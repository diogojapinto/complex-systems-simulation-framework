name := "Complex Systems Simulation Framework"

version := "1.0"

scalaVersion := "2.11.7"

lazy val versions = new {
  val akka = "2.4.2"
  val akkaStream = "2.0.3"
  val logback = "1.0.13"
  val scalaTest = "2.2.3"
  val deeplearning4j = "0.4-rc3.8"
  val canova = "0.0.0.14"
}

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % versions.akka,
  "com.typesafe.akka" %% "akka-agent" % versions.akka,
  "com.typesafe.akka" %% "akka-remote" % versions.akka,
  "com.typesafe.akka" %% "akka-slf4j" % versions.akka,
  "com.typesafe.akka" %% "akka-testkit" % versions.akka,
  "com.typesafe.akka" %% "akka-cluster-tools" % versions.akka,

  "com.typesafe.akka" %% "akka-stream" % versions.akka,
  "com.typesafe.akka" %% "akka-http-core" % versions.akka,
  "com.typesafe.akka" % "akka-http-experimental_2.11" % versions.akka,
  "com.typesafe.akka" %% "akka-http-testkit" % versions.akka,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % versions.akka,
  //"com.typesafe.akka" %% "akka-http-xml-experimental" % versions.akka,

  "org.scalatest" %% "scalatest" % versions.scalaTest % "test",

  "org.deeplearning4j" % "deeplearning4j-core" % versions.deeplearning4j,
  "org.nd4j" % "nd4j-x86" % versions.deeplearning4j,
  "org.deeplearning4j" % "deeplearning4j-ui" % versions.deeplearning4j,
  "org.nd4j" % "canova-api" % versions.canova
)