ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "boilerplate4s",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.6",
      "dev.zio" %% "zio-test" % "2.0.6" % Test
    )
  )
