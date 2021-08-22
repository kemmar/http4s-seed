package com.fiirb

import cats.effect.{ConcurrentEffect, ExitCode, IO, IOApp, Timer}
import com.fiirb.endpoint.{CSCardsEndpoint, ScoredCardsEndpoint}
import com.fiirb.service.CreditCardService
import com.fiirb.util.{AppConfig, ErrorHandler, Services}
import org.http4s.client.middleware.Logger
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object Application extends IOApp with Services {

  def service[F[_]: ConcurrentEffect](implicit timer: Timer[F]): F[Unit] = {
    for {
      client <- blazeClient

      loggedClient = Logger(logBody = true, logHeaders = true)(client)

      cSCardsService = new CSCardsEndpoint(loggedClient)
      scoredCardsService = new ScoredCardsEndpoint(loggedClient)

      creditCardService = new CreditCardService(cSCardsService, scoredCardsService)

      exitCode <- BlazeServerBuilder(global)
        .bindHttp(AppConfig.basePort, "0.0.0.0")
        .withHttpApp(routeAggregator(creditCardService).routes)
        .withServiceErrorHandler(new ErrorHandler().errorHandler)
        .serve
    } yield exitCode
  }.compile
    .drain

  def run(args: List[String]): IO[ExitCode] = service[IO].as(ExitCode.Success)
}
