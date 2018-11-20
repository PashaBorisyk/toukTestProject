package models

import java.sql.Date

import models.DriverType.DriverType
import slick.ast.BaseTypedType
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcType

import scala.math._


/** Data model for customer's parking record.
  *
  * @param vehicleRegistryNumber Customer's vehicle registry number
  * @param driver                Customers driver type
  * @param parkingEndTime        The time customer wants to stop his parking period
  * @param parkingStartTime      The time customer starts his parking period
  * @param id                    Record id in database
  */
case class CustomerParkingRecord(
                                   vehicleRegistryNumber: String,
                                   driver: DriverType.Value,
                                   parkingEndTime: Option[Date] = None,
                                   parkingStartTime: Date = new Date(System.currentTimeMillis()),
                                   id: Long = 0L
                                ) extends Serializable {
   
   /** Calculates costs for current parking record.
     *
     * If [[CustomerParkingRecord.parkingEndTime]] is not
     * filled yet then actual date will be used.
     *
     * @return actual parking costs
     */
   def getCosts(): Double = {
      
      val parkingTime = parkingEndTime.getOrElse(
         new Date(System.currentTimeMillis())
      ).getTime - parkingStartTime.getTime
      
      val hours: Long = parkingTime / Periods.HOUR
      
      print(hours)
      
      driver match {
         case DriverType.REGULAR =>
            if (hours == 0) {
               0
            } else {
               1 + countGeometricProgressionSum(2, 1.5f, hours - 1)
            }
         case DriverType.DISABLED => {
            if (hours == 0) {
               0
            } else {
               countGeometricProgressionSum(2, 1.2f, hours - 1)
            }
         }
      }
      
   }
   
   /** Calculates the sum for geometric progression
     *
     * @param firstElement  first element of progression
     * @param multiply      common ratio
     * @param elementsCount number of elements of that progression
     * @return the sum of progression
     */
   private def countGeometricProgressionSum(firstElement: Long, multiply: Double, elementsCount: Long): Double = {
      firstElement * (pow(multiply, elementsCount) - 1) / (multiply - 1)
   }
   
}

/** Database repository for [[CustomerParkingRecord]]
  *
  * @param tag [[Tag]]
  */
class CustomerParkingRecordRepository(tag: Tag)
   extends Table[CustomerParkingRecord](tag, "customer_parking_record") {
   
   /** Maps [[DriverType]] to [[String]] as database knows nothing about [[Enumeration]]
     *
     */
   implicit val driverTypeMapper: JdbcType[DriverType] with BaseTypedType[DriverType] =
      MappedColumnType.base[DriverType, String](
         e => e.toString,
         s => DriverType.withName(s)
      )
   
   /** [[CustomerParkingRecordRepository]] table representation
     *
     * @return function for converting [[CustomerParkingRecordRepository]]
     *         to table representation and vice versa
     */
   def * = (
      vehicleRegistryNumber,
      driver,
      parkingEndTime,
      parkingStartTime,
      id
   ) <> (CustomerParkingRecord.tupled, CustomerParkingRecord.unapply)
   
   /** [[CustomerParkingRecordRepository.id]] column representation
     *
     * @return presisted [[CustomerParkingRecordRepository.id]]
     */
   def id = column[Long]("id", O.AutoInc, O.PrimaryKey)
   
   /** [[CustomerParkingRecordRepository.driver]] column representation
     *
     * @return presisted [[CustomerParkingRecordRepository.driver]]
     */
   def driver = column[DriverType]("driver")
   
   /** [[CustomerParkingRecordRepository.vehicleRegistryNumber]] column representation
     *
     * @return presisted [[CustomerParkingRecordRepository.vehicleRegistryNumber]]
     */
   def vehicleRegistryNumber = column[String]("vehicle_registry_number")
   
   /** [[CustomerParkingRecordRepository.parkingStartTime]] column representation
     *
     * @return presisted [[CustomerParkingRecordRepository.parkingStartTime]]
     */
   def parkingStartTime = column[Date]("parking_start_time")
   
   /** [[CustomerParkingRecordRepository.parkingEndTime]] column representation
     *
     * @return presisted [[CustomerParkingRecordRepository.parkingEndTime]]
     */
   def parkingEndTime = column[Option[Date]]("parking_end_time")
   
}

/** Driver type Enumeration */
object DriverType extends Enumeration {
   type DriverType = Value
   val REGULAR: Value = Value("REGULAR")
   val DISABLED: Value = Value("DISABLED")
}

