package com.fiirb.util

import org.http4s.HttpRoutes

abstract class BaseController[F[_]] {
  def route: HttpRoutes[F]
}
