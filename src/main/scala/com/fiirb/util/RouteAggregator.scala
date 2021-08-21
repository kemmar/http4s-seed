package com.fiirb
package util

import cats.effect.ConcurrentEffect
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT


class RouteAggregator[F[_]: ConcurrentEffect](routesList: HttpRoutes[F]*) {

  val routes = routesList.reduce(_ <+> _).orNotFound

}
