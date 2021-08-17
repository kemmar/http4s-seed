package com.fiirb.util

import cats.effect.IO
import org.http4s.HttpRoutes

abstract class BaseController {

  def route: HttpRoutes[IO]

}
