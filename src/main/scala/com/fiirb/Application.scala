package com.fiirb

import cats.effect.{ExitCode, IO, IOApp}
import com.fiirb.service.Services
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object Application extends IOApp with Services {

  def run(args: List[String]): IO[ExitCode] = {
    for {
      exitCode <- BlazeServerBuilder[IO](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(routeAggregator[IO]().routes)
        .serve
    } yield exitCode
  }.compile
    .drain
    .as(ExitCode.Success)
}
