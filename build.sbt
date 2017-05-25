import sbt.Keys._

val amazonSdkVersion = "1.11.109"
val akkaVersion = "2.5.1"
val akkaHttpVersion = "10.0.5"
val awsSdkVersion = "1.11.109"

val commonSettings = Seq(
  organization  := "com.gilt",
  startYear     := Some(2017),
  version       := "0.1.0",
  licenses      += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
  scalaVersion  := "2.11.11",
  crossScalaVersions := Seq(scalaBinaryVersion.value, "2.12.2")
)

lazy val akka = (project in file("akka"))
  .settings(commonSettings)
  .settings(
    name        := "gfc-aws-s3-akka",
    description := "Library to handle data streaming to and from s3",
    libraryDependencies ++= Seq(
      "org.slf4j"          % "slf4j-api"        % "1.7.25",
      "com.amazonaws"      %  "aws-java-sdk-s3" % awsSdkVersion,
      "com.typesafe.akka"  %% "akka-stream"     % akkaVersion,

      "org.scalatest"     %% "scalatest"                   % "3.0.1" % Test,
      "org.scalamock"     %% "scalamock-scalatest-support" % "3.6.0" % Test
    )
  )

lazy val s3http = (project in file("s3-http"))
  .settings(commonSettings)
  .settings(
    name            := "s3-http",
    description     := "Small HTTP service serving static content from s3",
    publishArtifact := false,
    dockerBaseImage := "openjdk:8-jre",
    libraryDependencies ++= Seq(
      "com.typesafe"      %  "config"          % "1.3.1",
      "com.typesafe.akka" %% "akka-http"       % akkaHttpVersion,
      "com.amazonaws"     %  "aws-java-sdk-s3" % amazonSdkVersion,
      "ch.qos.logback"    %  "logback-classic" % "1.2.3",

      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "org.scalatest"     %% "scalatest"         % "3.0.1" % Test,
      "io.findify"        %% "s3mock"            % "0.2.2" % Test
    )
  ).dependsOn(akka)
  .enablePlugins(JavaAppPackaging)


lazy val root = (project in file("."))
  .settings(
    name := "gfc-aws-s3",
    publishArtifact := false
  ).aggregate(akka, s3http)
