name := "SimpleServiceAPI"

version := "0.1"

scalaVersion := "2.13.6"

val http4sVersion = "0.23.6"
val catsCoreVersion = "2.6.1"
val catsEffectVersion = "3.2.9"
val CirceVersion = "0.14.1"

libraryDependencies ++= Seq(
  "org.http4s"      %% "http4s-dsl"           % http4sVersion,
  "org.http4s"      %% "http4s-blaze-server"  % http4sVersion,
  "org.http4s"      %% "http4s-blaze-client"  % http4sVersion,
  "org.http4s"      %% "http4s-circe"         % http4sVersion,
  "org.typelevel"   %% "cats-core"            % catsCoreVersion,
  "org.typelevel"   %% "cats-effect"          % catsEffectVersion,
  "io.circe"        %% "circe-generic"        % CirceVersion,
)

//scalacOptions ++= Seq("-Ypartial-unification")