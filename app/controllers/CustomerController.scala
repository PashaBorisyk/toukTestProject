package controllers

import javax.inject._
import models.CustomerParkingRecord
import play.api.libs.json.Json
import play.api.mvc._
import services.traits.CustomerParkingRecordService

import scala.concurrent.ExecutionContext

/** Http controller for customer requests */
@Singleton
class CustomerController @Inject()(
                                     cc: ControllerComponents,
                                     parkingRecordService: CustomerParkingRecordService
                                  )(implicit ec: ExecutionContext) extends AbstractController(cc) {
   
   /** Start parking period for customer
     *
     * @param carRegistryNumber customers registry number
     * @param driverType        customers driver type
     * @return new [[models.CustomerParkingRecord]] record
     */
   def startParkingPeriod(carRegistryNumber: String, driverType: String) = Action.async {
      val recordFuture = parkingRecordService.startParkingPeriod(carRegistryNumber, driverType)
      recordFuture.map {
         record =>
            Ok(Json.obj(
               "id" -> record.id,
               "carRegistryNumber" -> record.vehicleRegistryNumber,
               "parkingStartTime" -> record.parkingStartTime.toString
            ))
      }.recover {
         case e: Exception =>
            e.printStackTrace()
            InternalServerError(e.getMessage)
      }
   }
   
   /** Returns current parking bill for this vehicle registry number
     *
     * @param carRegistryNumber customers car registry number
     * @return current parking bill
     */
   def getActualParkingAmount(carRegistryNumber: String) = Action.async {
      val amountFuture = parkingRecordService.getActualParkingAmount(carRegistryNumber)
      amountFuture.map {
         amount =>
            Ok(Json.obj(
               "actual_amount" -> amount
            ))
      }.recover {
         case _: NoSuchElementException =>
            NotFound(s"No such element in database for carRegistryNumber : $carRegistryNumber")
         case e: Exception =>
            e.printStackTrace()
            InternalServerError(e.getMessage)
      }
   }
   
   /** Stops customer parking period
     *
     * @param vehicleRegistryNumber customers vehicle registry number
     * @return final [[CustomerParkingRecord]]
     */
   def stopParkingPeriod(carRegistryNumber: String) = Action.async {
      val parkingRecordFuture = parkingRecordService.stopParkingPeriod(carRegistryNumber)
      parkingRecordFuture.map {
         parkingRecord =>
            Ok(Json.obj(
               "id" -> parkingRecord.id,
               "parkingStartTime" -> parkingRecord.parkingStartTime,
               "parkingEndTime" -> parkingRecord.parkingEndTime,
               "carRegistryNumber" -> parkingRecord.vehicleRegistryNumber,
               "total_amount" -> parkingRecord.getCosts()
            ))
      }.recover {
         case _: NoSuchElementException =>
            NotFound(s"No such element in database for carRegistryNumber : $carRegistryNumber")
         case e: Exception =>
            e.printStackTrace()
            InternalServerError(e.getMessage)
      }
   }
   
}
