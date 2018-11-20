package controllers

import javax.inject.{Inject, _}
import models.CustomerParkingRecord
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.traits.CustomerParkingRecordService

import scala.concurrent.ExecutionContext

/** Http controller for operator requests */
@Singleton
class OperatorController @Inject()(
                                     cc: ControllerComponents,
                                     parkingRecordService: CustomerParkingRecordService
                                  )(implicit ec: ExecutionContext) extends AbstractController(cc) {
   
   /** Returns true if there is [[CustomerParkingRecord]] for this car registry number
     *
     * @param carRegistryNumber customers vehicle registry number
     * @return true if record exists. false otherwise
     */
   def getIsCarRegistered(date: String) = Action.async {
      val result = parkingRecordService.getIsCarRegistered(date)
      result.map {
         isRegistered =>
            Ok(Json.obj("is_registered" -> isRegistered))
      }.recover {
         case _: NoSuchElementException =>
            NotFound(s"No such element in database for date : $date")
         case e: Exception =>
            e.printStackTrace()
            InternalServerError(e.getMessage)
      }
   }
   
}
