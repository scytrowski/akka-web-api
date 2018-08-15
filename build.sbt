import Dependencies._

lazy val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.14",
  "com.typesafe.akka" %% "akka-stream" % "2.5.14",
  "com.typesafe.akka" %% "akka-http" % "10.1.3"
)

lazy val testDependencies = Seq(
  scalaTest % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.3" % Test
)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "akka-web-api",
    libraryDependencies ++= akkaDependencies ++ testDependencies
  )
