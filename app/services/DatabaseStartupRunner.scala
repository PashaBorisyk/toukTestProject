package services

import javax.inject.{Inject, Singleton}
import models.CustomerParkingRecordRepository
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.ControllerComponents
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

/** Runs database actions on application startup */
@Singleton
class DatabaseStartupRunner @Inject()(
                                        protected val dbConfigProvider: DatabaseConfigProvider,
                                        cc: ControllerComponents
                                     )(implicit ec: ExecutionContext)
   extends HasDatabaseConfigProvider[JdbcProfile] {
   
   private val schema = TableQuery[CustomerParkingRecordRepository].schema
   
   db.run(DBIO.seq(
      schema.create
   )).map(_ => println("Schema customer_parking_record created"))
   
}