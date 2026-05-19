import xerial.sbt.Sonatype.sonatypeCentralHost

lazy val testContainersScalaVersion = "0.44.1"
lazy val testContainersGitlabVersion = "1.1.0"
lazy val akkaVersion = "2.6.17"

ThisBuild / semanticdbVersion := "4.12.3"
ThisBuild / sonatypeCredentialHost := sonatypeCentralHost
ThisBuild / versionScheme := Some("early-semver")

lazy val root = (project in file(".")).settings(
  organization := "com.clever-cloud",
  scalaVersion := "2.13.6",
  version := "1.0.4",
  name := "testcontainers-scala-gitlab",
  licenses := List("MIT" -> new URI("https://mit-license.org/").toURL()),
  homepage := Some(url("https://github.com/CleverCloud/testcontainers-scala-gitlab")),
  resolvers += Resolver.mavenLocal,
  resolvers += Resolver.mavenCentral,
  libraryDependencies ++= Seq(
    "com.dimafeng" %% "testcontainers-scala-core" % testContainersScalaVersion,
    "com.clever-cloud" % "testcontainers-gitlab" % testContainersGitlabVersion,

    "org.scalatest" %% "scalatest" % "3.2.10" % Test,
    "com.typesafe.akka" %% "akka-protobuf-v3" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
    "com.dimafeng" %% "testcontainers-scala-scalatest" % testContainersScalaVersion % Test,
    "io.moia" %% "scala-http-client" % "4.7.0" % Test,
  ),
  Test / publishArtifact := false,
  pomIncludeRepository := { _ => false },
  developers := List(
    Developer("blackyoup", "Arnaud Lefebvre", "arnaud.lefebvre@clever-cloud.com", url("https://github.com/blackyoup"))
  ),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/CleverCloud/testcontainers-scala-gitlab"),
      "scm:git:git@github.com:CleverCloud/testcontainers-scala-gitlab"
    )
  ),
  Test / fork := true,
  usePgpKeyHex("B11C53C05D413713BDD3660FA7B8F38C536F1DF2"),
)
