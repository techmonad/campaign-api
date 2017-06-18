import sbt._

object Dependencies {

  lazy val dependencies =
    Seq(
      "com.typesafe.akka" %% "akka-http" % "10.0.7",
      "org.json4s" %% "json4s-native" % "3.4.0",
      "ch.qos.logback" % "logback-classic" % "1.1.7",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.0.7" % "test,it",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test,it"
    )
}
