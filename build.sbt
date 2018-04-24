name := "telesurveillance"

version := "1.0"

scalaVersion := "2.12.4"

lazy val akkaVersion = "2.5.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  //"org.iq80.leveldb" % "leveldb" % "0.7",
  //"org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc41",
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "ch.qos.logback"% "logback-classic" % "1.2.3"
)