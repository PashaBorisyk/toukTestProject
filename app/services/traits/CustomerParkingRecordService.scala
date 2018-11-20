package services.traits

import com.google.inject.ImplementedBy
import models.CustomerParkingRecord
import services.CustomerParkingRecordServiceImpl

import scala.concurrent.Future

/** Data Access Object for [[CustomerParkingRecord]] */
@ImplementedBy(classOf[CustomerParkingRecordServiceImpl])
trait CustomerParkingRecordService {
   
   /** Creates new [[CustomerParkingRecord]]
     *
     * @param carRegistryNumber customers vehicle registry number
     * @param driverType        type of a customer. See [[models.DriverType]]
     * @return [[Future]] with newly created [[CustomerParkingRecord]]
     */
   def startParkingPeriod(carRegistryNumber: String, driverType: String): Future[CustomerParkingRecord]
   
   /** Stops customer parking period
     *
     * @param vehicleRegistryNumber customers vehicle registry number
     * @return [[Future]] with final [[CustomerParkingRecord]]
     */
   def stopParkingPeriod(vehicleRegistryNumber: String): Future[CustomerParkingRecord]
   
   /** Returns true if there is [[CustomerParkingRecord]] for this car registry number
     *
     * @param carRegistryNumber customers vehicle registry number
     * @return [[Future]] with true if record exists. false otherwise
     */
   def getIsCarRegistered(carRegistryNumber: String): Future[Boolean]
   
   /** Returns current parking bill for this vehicle registry number
     *
     * @param carRegistryNumber customers vehicle registry number
     * @return [[Future]] with current parking bill
     */
   def getActualParkingAmount(carRegistryNumber: String): Future[Double]
   
   /** Returns the amount that parking owner has earned during given day.
     *
     * @param dayDate day to make calculations for
     * @return [[Future]] with the amount that parking owner has earned
     */
   def getEarnedAmountForDay(dayDate: String): Future[Double]
   
}
