name := "toukTestProject"

version := "1.0"

lazy val `touktestproject` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(ehcache, ws, specs2 % Test, guice,
   "com.typesafe.slick" %% "slick" % "3.2.3",
   "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
   "com.typesafe.play" %% "play-slick" % "3.0.0",
   "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0"
)

libraryDependencies += "com.h2database" % "h2" % "1.4.192"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.powermock" % "powermock-module-junit4" % "2.0.0-RC.4" % Test


unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      