package com.fiirb.util

import cats.effect.{ConcurrentEffect, IO, IOApp}
import com.fiirb.controller.CreditCardController
import com.fiirb.service.CreditCardService
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.dsl.Http4sDsl

import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._

trait Services {
  self: IOApp =>

  lazy val blazeClient: fs2.Stream[IO, Client[IO]] =
    BlazeClientBuilder[IO](global)
      .withRequestTimeout(15.seconds)
      .stream

  implicit def dsl[F[_]] = new Http4sDsl[F] {}

  def routeAggregator[F[_] : ConcurrentEffect](creditCardService: CreditCardService[F]) =
    new RouteAggregator(
      new CreditCardController(creditCardService).route
    )

}
