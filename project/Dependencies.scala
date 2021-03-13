import sbt._

object Dependencies {

  lazy val dependencies =
    Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.5.13",
      "com.typesafe.akka" %% "akka-stream" % "2.5.13",
      "com.typesafe.akka" %% "akka-http" % "10.1.14",
      "org.json4s" %% "json4s-native" % "3.5.4",
      "ch.qos.logback" % "logback-classic" % "1.1.7",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.1.14" % "test,it",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test,it"
    )
}
