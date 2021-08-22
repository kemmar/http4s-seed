package com.fiirb.service

import cats.effect._
import com.fiirb.controller.WuxiaController
import com.fiirb.endpoint.ReadNovelsHomeEndpoint
import com.fiirb.util.RouteAggregator
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.dsl.Http4sDsl
import cats.implicits._

import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._

trait Services {
  self: IOApp =>

  lazy val blazeClient: fs2.Stream[IO, Client[IO]] =
    BlazeClientBuilder[IO](global)
      .withRequestTimeout(15.seconds)
      .stream

  implicit def dsl[F[_]] = new Http4sDsl[F] {}

  def routeAggregator[F[_]: ConcurrentEffect](): RouteAggregator[F] = {
    lazy val readNovelsHomeEndpoint = new ReadNovelsHomeEndpoint[F]()
    lazy val wuxiaService = new WuxiaService[F](readNovelsHomeEndpoint)

    new RouteAggregator(
      new WuxiaController[F](wuxiaService).route
    )
  }

}
