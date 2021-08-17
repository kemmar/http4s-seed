package com.fiirb

import cats.effect.{ExitCode, IO, IOApp}
import com.fiirb.service.Services
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends IOApp with Services {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(routeAggregator.routes)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
