import sbt._

object Dependencies {

  lazy val dependencies =
    Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.6.17",
      "com.typesafe.akka" %% "akka-stream" % "2.6.17",
      "com.typesafe.akka" %% "akka-http" % "10.2.6",
      "org.json4s" %% "json4s-native" % "4.0.3",
      "ch.qos.logback" % "logback-classic" % "1.2.6",
      "com.typesafe.akka" %% "akka-testkit" % "2.6.17" % "test,it",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.2.6" % "test,it",
      "org.scalatest" %% "scalatest" % "3.2.11" % "test,it"
    )
}
