package com.fiirb.controller

import cats.effect.IO
import com.fiirb.domain.UserInformation
import com.fiirb.util.BaseController
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._

import scala.concurrent.ExecutionContext

class CreditCardController()(implicit ec: ExecutionContext) extends BaseController {

  val creditCardRoutes = HttpRoutes.of[IO] {
    case req@POST -> Root / "creditcards" =>
      for {
        userinfo <- req.as[UserInformation]
        resp <- Ok(userinfo.asJson)
      } yield resp
  }

  val route: HttpRoutes[IO] = creditCardRoutes
}
