name := "Complex Systems Simulation Framework"

version := "1.0"

scalaVersion := "2.11.7"

lazy val versions = new {
  val finatra = "2.1.3"
  val akka = "2.4.2"
  val guice = "4.0"
  val logback = "1.0.13"
  val scalaz = "7.2.0"
}

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % versions.akka,
  "com.typesafe.akka" %% "akka-agent" % versions.akka,
  "com.typesafe.akka" %% "akka-remote" % versions.akka,
  "com.typesafe.akka" %% "akka-slf4j" % versions.akka,
  "com.typesafe.akka" %% "akka-testkit" % versions.akka,
  "com.typesafe.akka" %% "akka-cluster-tools" % versions.akka,

  "com.twitter.finatra" %% "finatra-http" % versions.finatra,
  "com.twitter.finatra" %% "finatra-httpclient" % versions.finatra,
  "com.twitter.finatra" %% "finatra-slf4j" % versions.finatra,
  "com.twitter.inject" %% "inject-core" % versions.finatra,
  "ch.qos.logback" % "logback-classic" % versions.logback,

  "com.twitter.finatra" %% "finatra-http" % versions.finatra % "test",
  "com.twitter.finatra" %% "finatra-jackson" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-server" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-app" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-core" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-modules" % versions.finatra % "test",
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",

  "com.twitter.finatra" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-jackson" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-modules" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-server" % versions.finatra % "test" classifier "tests",

  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.scalatest" %% "scalatest" % "2.2.3" % "test",
  "org.specs2" %% "specs2" % "2.3.12" % "test"
)