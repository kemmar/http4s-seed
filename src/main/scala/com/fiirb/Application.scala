package com.fiirb

import cats.effect.{ConcurrentEffect, ExitCode, IO, IOApp, Timer}
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global
import com.fiirb.util.{AppConfig, ErrorHandler, Services}

object Application extends IOApp with Services {

  def service[F[_]: ConcurrentEffect](implicit timer: Timer[F]): F[Unit] = {
    for {
      exitCode <- BlazeServerBuilder(global)
        .bindHttp(AppConfig.basePort, "0.0.0.0")
        .withHttpApp(routeAggregator().routes)
        .withServiceErrorHandler(new ErrorHandler().errorHandler)
        .serve
    } yield exitCode
  }.compile
    .drain

  def run(args: List[String]): IO[ExitCode] = service[IO].as(ExitCode.Success)
}
