import Dependencies._

lazy val commonSettings =
  Seq(
    name := "campaign-api",
    version := "1.0",
    scalaVersion := "2.12.13",
    organization := "com.techmonad"
  )

lazy val root =
  (project in file(".")).
    configs(IntegrationTest).
    settings(commonSettings: _*).
    settings(Defaults.itSettings: _*).
    settings(
      libraryDependencies ++= dependencies
    )
