import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.shafiquejamal",
      scalaVersion := "2.12.4",
      version      := "0.0.1"
    )),
    name := "gis-util",
    libraryDependencies ++= Seq(scalaTest % Test)
  )
