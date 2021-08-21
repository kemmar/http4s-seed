package com.fiirb

import cats.effect.{ExitCode, IO, IOApp}
import com.fiirb.service.{CSCardsService, CreditCardService, ScoredCardsService}
import com.fiirb.util.Services
import org.http4s.client.middleware.Logger
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object Application extends IOApp with Services {

  def run(args: List[String]): IO[ExitCode] = {
    for {
      client <- blazeClient

      loggedClient = Logger(logBody = true, logHeaders = true)(client)

      cSCardsService = new CSCardsService[IO](loggedClient)
      scoredCardsService = new ScoredCardsService[IO](loggedClient)

      creditCardService = new CreditCardService[IO](cSCardsService, scoredCardsService)

      exitCode <- BlazeServerBuilder[IO](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(routeAggregator(creditCardService).routes)
        .serve
    } yield exitCode
  }.compile
    .drain
    .as(ExitCode.Success)
}
