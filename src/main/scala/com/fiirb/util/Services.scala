package com.fiirb.util

import cats.effect.{ConcurrentEffect, IOApp}
import com.fiirb.controller.WuxiaController
import com.fiirb.endpoint.{ReadNovelsChapterEndpoint, ReadNovelsChapterListEndpoint, ReadNovelsHomeEndpoint, ReadNovelsSearchEndpoint}
import com.fiirb.service.WuxiaService
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.dsl.Http4sDsl

import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._

trait Services {
  self: IOApp =>

  def blazeClient[F[_] : ConcurrentEffect]: fs2.Stream[F, Client[F]] =
    BlazeClientBuilder[F](global)
      .withRequestTimeout(15.seconds)
      .withConnectTimeout(2.seconds)
      .stream

  implicit def dsl[F[_]]: Http4sDsl[F] = new Http4sDsl[F] {}

  def routeAggregator[F[_] : ConcurrentEffect](): RouteAggregator[F] = {
    lazy val readNovelsHomeEndpoint = new ReadNovelsHomeEndpoint()

    lazy val readNovelsSearchEndpoint = new ReadNovelsSearchEndpoint()

    lazy val readNovelsChapterListEndpoint = new ReadNovelsChapterListEndpoint()
    lazy val readNovelsChapterEndpoint = new ReadNovelsChapterEndpoint()

    lazy val wuxiaService =
      new WuxiaService[F](readNovelsHomeEndpoint, readNovelsSearchEndpoint, readNovelsChapterListEndpoint, readNovelsChapterEndpoint)

    new RouteAggregator(
      new WuxiaController(wuxiaService).route
    )
  }

}
