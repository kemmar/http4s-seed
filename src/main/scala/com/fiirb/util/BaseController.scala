package com.fiirb.util

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

abstract class BaseController[F[_]] {
  def route: HttpRoutes[F]

}
