package com.fiirb.service

import cats.effect.IOApp
import com.fiirb.controller.CreditCardController
import com.fiirb.util.RouteAggregator

import scala.concurrent.ExecutionContext

trait Services {
  self: IOApp =>

  implicit def ec: ExecutionContext

  val creditCardController = new CreditCardController()

  val routeAggregator =
    new RouteAggregator(
      creditCardController.route
    )

}
