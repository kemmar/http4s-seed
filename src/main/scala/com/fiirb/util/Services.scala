package com.fiirb.util

import cats.effect.{ConcurrentEffect, IOApp}
import com.fiirb.controller.CreditCardController
import com.fiirb.service.CreditCardService
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.dsl.Http4sDsl

import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._

trait Services {
  self: IOApp =>

  def blazeClient[F[_]: ConcurrentEffect]: fs2.Stream[F, Client[F]] =
    BlazeClientBuilder[F](global)
      .withRequestTimeout(15.seconds) // would normally make these configurable in the application.conf
      .withConnectTimeout(2.seconds)
      .stream

  implicit def dsl[F[_]]: Http4sDsl[F] = new Http4sDsl[F] {}

  def routeAggregator[F[_] : ConcurrentEffect](creditCardService: CreditCardService[F]) =
    new RouteAggregator(
      new CreditCardController(creditCardService).route
    )

}
