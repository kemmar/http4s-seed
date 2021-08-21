name := "http4s-seed"

version := "0.1"

scalaVersion := "2.13.6"

val http4sVersion = "0.21.25"
val MunitVersion = "0.7.27"
val MunitCatsEffectVersion = "1.0.5"
val circeVersion = "0.14.1"
val scalaTestVersion = "3.2.0"
val mockitoVersion = "1.16.37"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.1",
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-literal" % circeVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "org.mockito" %% "mockito-scala" % mockitoVersion % Test,
  "org.scalameta" %% "munit" % MunitVersion % Test,
  "org.typelevel" %% "munit-cats-effect-2" % MunitCatsEffectVersion % Test,
)
