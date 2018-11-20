package services

import java.sql.Date
import java.text.{ParseException, SimpleDateFormat}

import com.google.inject.Inject
import models.{CustomerParkingRecord, CustomerParkingRecordRepository, DriverType, Periods}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.traits.CustomerParkingRecordService
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcProfile

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

/** @inheritdoc */
class CustomerParkingRecordServiceImpl @Inject()(
                                                   protected val dbConfigProvider: DatabaseConfigProvider
                                                )(implicit ec: ExecutionContext)
   extends HasDatabaseConfigProvider[JdbcProfile] with CustomerParkingRecordService {
   
   private val parkingRecordTable = TableQuery[CustomerParkingRecordRepository]
   
   /** @inheritdoc */
   def startParkingPeriod(carRegistryNumber: String, driverType: String): Future[CustomerParkingRecord] = {
      
      val query = parkingRecordTable returning parkingRecordTable.map(_.id)
      
      val customerParkingRecord = CustomerParkingRecord(
         carRegistryNumber, DriverType.withName(driverType)
      )
      db.run(query += customerParkingRecord).map {
         id => customerParkingRecord.copy(id = id)
      }
   }
   
   /** @inheritdoc */
   def stopParkingPeriod(vehicleRegistryNumber: String): Future[CustomerParkingRecord] = {
      
      db.run(parkingRecordTable.filter {
         record => record.vehicleRegistryNumber === vehicleRegistryNumber
      }.result.head.map {
         record =>
            
            val updatedRecord = record.copy(
               parkingEndTime = Some(new Date(System.currentTimeMillis()))
            )
            
            Await.result(
               db.run(
                  parkingRecordTable.update(
                     updatedRecord
                  )
               ), 5.second
            )
            updatedRecord
      })
      
   }
   
   /** @inheritdoc */
   def getIsCarRegistered(carRegistryNumber: String): Future[Boolean] = {
      db.run(parkingRecordTable.filter(
         record => record.vehicleRegistryNumber === carRegistryNumber
      ).exists.result)
   }
   
   /** @inheritdoc */
   def getActualParkingAmount(vehicleRegistryNumber: String): Future[Double] = {
      db.run(parkingRecordTable.filter(
         record => record.vehicleRegistryNumber === vehicleRegistryNumber
      ).result.head.map {
         record =>
            record.getCosts()
      })
   }
   
   /** @inheritdoc */
   def getEarnedAmountForDay(dayDate: String): Future[Double] = {
      
      val dateFormat = new SimpleDateFormat("dd-mm-yyyy")
      var dateStart: Date = null
      
      try {
         dateStart = new Date(dateFormat.parse(dayDate).getTime)
      } catch {
         case e: ParseException =>
            e.printStackTrace()
            dateStart = new Date(System.currentTimeMillis())
      }
      
      val dateEnd = new Date(dateStart.getTime + Periods.DAY)
      
      db.run(parkingRecordTable.filter {
         record =>
            record.parkingStartTime >= dateStart &&
               record.parkingEndTime < dateEnd
      }.result.map {
         recordSeq =>
            recordSeq.foldLeft(0.0)(_ + _.getCosts())
      })
   }
   
}
