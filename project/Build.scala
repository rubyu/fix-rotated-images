import sbt.Keys._
import sbt._

object Build extends sbt.Build {
  lazy val commonSettings =
    Defaults.coreDefaultSettings ++
      Seq(
        version := "0.1",
        scalaVersion := "2.11.8",
        organization := "com.github.rubyu",
        name := "fix-rateted-images"
      )

  lazy val project =
    Project("main", file("."))
      .settings(commonSettings: _*)
      .settings(Seq(
        fork := true,
        unmanagedBase := baseDirectory.value / "lib",
        javaOptions += "-Djava.library.path=" + unmanagedBase.value
      ))
      .settings(Seq(
        scalacOptions := Seq(
          "-deprecation",
          "-unchecked",
          "-feature"
        )
      ))
      .settings(
        libraryDependencies ++= Seq(
          //
        )
      )
}
