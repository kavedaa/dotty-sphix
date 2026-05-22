name := "sphix"

organization := "org.sphix"

version := "1.21.13.96-SNAPSHOT"

scalaVersion := "3.3.4"

resolvers += "Vedaa Data Public" at "https://mymavenrepo.com/repo/UulFGWFKTwklJGmfuD8D/"

//  JavaFX

libraryDependencies ++= Seq(
  "org.openjfx" % "javafx-controls" % "21.0.1",
  "org.openjfx" % "javafx-web" % "21.0.1",
  "org.openjfx" % "javafx-fxml" % "21.0.1")

//  ControlsFX

libraryDependencies += "org.controlsfx" % "controlsfx" % "11.2.0"

//  Text/Excel utilities

libraryDependencies += "no.vedaadata" %% "text-util" % "1.1.0"
libraryDependencies += "no.vedaadata" %% "excel-util" % "0.9.2"

//  Various test/demo dependencies

libraryDependencies += "no.vedaadata" %% "generator-util" % "0.9.6" % "test"
libraryDependencies += "no.vedaadata" %% "xml-util" % "0.9.2" % "test"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test"

Test / run / fork := true

publishTo := Some("Vedaa Data Public publisher" at "https://mymavenrepo.com/repo/zPAvi2SoOMk6Bj2jtxNA/")

scalacOptions ++= Seq("-explain", "-feature", "-deprecation")