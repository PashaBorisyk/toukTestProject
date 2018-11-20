package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import services.traits.CustomerParkingRecordService

import scala.concurrent.ExecutionContext

/** Http controller for owner requests */
@Singleton
class OwnerController @Inject()(
                                  cc: ControllerComponents,
                                  parkingRecordService: CustomerParkingRecordService
                               )(implicit ec: ExecutionContext) extends AbstractController(cc) {
   
   /** Returns the amount that parking owner has earned during given day.
     *
     * @param date day to make calculations for
     * @return the amount that parking owner has earned
     */
   def getEarnedAmountForDay(date: String): Action[AnyContent] = Action.async {
      val result = parkingRecordService.getEarnedAmountForDay(date)
      result.map {
         amount =>
            Ok(Json.obj("total_amount" -> amount))
      }.recover {
         case _: NoSuchElementException =>
            NotFound(s"No such element in database for date : $date")
         case e: Exception =>
            e.printStackTrace()
            InternalServerError(e.getMessage)
      }
   }
   
}
