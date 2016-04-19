

name := "Complex Systems Simulation Framework"

version := "1.0"

scalaVersion := "2.11.7"

lazy val versions = new {
  val scalaReflect = "2.11.7"
  val scalaXml = "1.0.5"
  val akka = "2.4.4"
  val akkaStream = "2.4.4"
  val logback = "1.0.13"
  val scalaTest = "2.2.3"
  val deeplearning4j = "0.4-rc3.8"
  val canova = "0.0.0.14"
  val play = "2.5.0"
  val tepkin = "0.6"
  val log4j = "1.2.17"
}

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % versions.scalaXml,
  "org.scala-lang" % "scala-reflect" % versions.scalaReflect,

  "log4j" % "log4j" % versions.log4j,

  "com.typesafe.akka" %% "akka-contrib" % versions.akka,
  "com.typesafe.akka" %% "akka-actor" % versions.akka,
  "com.typesafe.akka" %% "akka-agent" % versions.akka,
  "com.typesafe.akka" %% "akka-remote" % versions.akka,
  "com.typesafe.akka" %% "akka-slf4j" % versions.akka,
  "com.typesafe.akka" %% "akka-testkit" % versions.akka,
  "com.typesafe.akka" %% "akka-cluster-tools" % versions.akka,

  "com.typesafe.akka" %% "akka-stream" % versions.akka,
  "com.typesafe.akka" %% "akka-http-core" % versions.akka,
  "com.typesafe.akka" %% "akka-http-experimental" % versions.akka,
  "com.typesafe.akka" %% "akka-http-testkit" % versions.akka,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % versions.akka,
  "com.typesafe.akka" %% "akka-http-xml-experimental" % versions.akka,

  "com.typesafe.play" % "play-json_2.11" % versions.play,

  "com.github.jeroenr" %% "tepkin" % versions.tepkin,

  "org.scalatest" %% "scalatest" % versions.scalaTest % "test",

  "org.deeplearning4j" % "deeplearning4j-core" % versions.deeplearning4j/*,
  "org.deeplearning4j" % "deeplearning4j-nlp" % versions.deeplearning4j excludeAll(
    ExclusionRule("com.typesafe.akka", "akka-contrib"),
    ExclusionRule("com.typesafe.akka", "akka-actor"),
    ExclusionRule("com.typesafe.akka", "akka-cluster"),
    ExclusionRule("com.typesafe.akka", "akka-remote")
    ),
  "org.deeplearning4j" % "deeplearning4j-ui" % versions.deeplearning4j excludeAll(
    ExclusionRule("com.typesafe.akka", "akka-contrib"),
    ExclusionRule("com.typesafe.akka", "akka-actor"),
    ExclusionRule("com.typesafe.akka", "akka-cluster"),
    ExclusionRule("com.typesafe.akka", "akka-remote")
    ),
  "org.nd4j" % "nd4j-x86" % versions.deeplearning4j,
  "org.nd4j" % "canova-api" % versions.canova*/
)