
scalaVersion := "2.12.10"

name := "neptune-demo-scala"
organization := "com.sezzle"
version := "0.1"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "org.apache.tinkerpop" % "gremlin-driver" % "3.4.8",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.github.finagle" %% "finchx-core" % "0.31.0",
  "com.github.finagle" %% "finchx-circe" % "0.31.0",
  "io.circe" %% "circe-generic" % "0.9.0",
  "org.typelevel" %% "cats-effect" % "2.1.3",
  "org.typelevel" %% "cats-core" % "2.1.1",
  "com.twitter" %% "twitter-server" % "20.9.0",
)
