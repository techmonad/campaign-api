import sbt._

object Dependencies {

  lazy val dependencies =
    Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.5.32",
      "com.typesafe.akka" %% "akka-stream" % "2.5.32",
      "com.typesafe.akka" %% "akka-http" % "10.2.4",
      "org.json4s" %% "json4s-native" % "3.5.4",
      "ch.qos.logback" % "logback-classic" % "1.1.11",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.2.4" % "test,it",
      "org.scalatest" %% "scalatest" % "3.0.9" % "test,it"
    )
}
