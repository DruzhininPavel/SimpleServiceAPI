name := "SimpleServiceAPI"

version := "0.1"

scalaVersion := "2.13.6"

val http4sVersion = "0.23.6"
val catsCoreVersion = "2.6.1"
val catsEffectVersion = "3.2.9"
val CirceVersion = "0.14.1"
val ScalaTestVersion = "3.2.10"
//val elastic4sVersion = "6.8.20"
//
//libraryDependencies ++= Seq(
//  // recommended client for beginners
//  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion,
//  // test kit
//  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test"
//)

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "3.0.0"
libraryDependencies += "org.apache.kafka" % "kafka-streams" % "3.0.0"
libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "3.0.0"
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.17.0"
libraryDependencies ++= Seq(
  "org.http4s"      %% "http4s-dsl"           % http4sVersion,
  "org.http4s"      %% "http4s-blaze-server"  % http4sVersion,
  "org.http4s"      %% "http4s-blaze-client"  % http4sVersion,
  "org.http4s"      %% "http4s-circe"         % http4sVersion,
  "org.typelevel"   %% "cats-core"            % catsCoreVersion,
  "org.typelevel"   %% "cats-effect"          % catsEffectVersion,
  "com.typesafe"    % "config"                % "1.4.1",
  "io.circe"        %% "circe-generic"        % CirceVersion,
  "org.scalatest"   %% "scalatest"            % ScalaTestVersion % "test"
)