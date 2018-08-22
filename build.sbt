import Dependencies._

lazy val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.14",
  "com.typesafe.akka" %% "akka-stream" % "2.5.14",
  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.3"
)

lazy val jodaDependencies = Seq(
  "joda-time" % "joda-time" % "2.10"
)

lazy val configDependencies = Seq(
  "com.typesafe" % "config" % "1.3.3"
)

lazy val dbDependencies = Seq(
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "com.h2database" % "h2" % "1.4.197" % Test,
  "mysql" % "mysql-connector-java" % "8.0.12"
)

lazy val testDependencies = Seq(
  scalaTest % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.3" % Test
)

lazy val allDependencies = akkaDependencies ++
  jodaDependencies ++
  configDependencies ++
  dbDependencies ++
  testDependencies

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "akka-web-api",
    libraryDependencies ++= allDependencies
  )
