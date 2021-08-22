package com.fiirb.controller

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.user.UserInformation
import com.fiirb.error.Errors.RequestError
import com.fiirb.service.CreditCardService
import org.http4s.{HttpRoutes, InvalidMessageBodyFailure, MalformedMessageBodyFailure}
import org.http4s.dsl.Http4sDsl

import scala.util.control.NonFatal

class CreditCardController[F[_] : Sync](creditCardService: CreditCardService[F])(implicit dsl: Http4sDsl[F]) {

  import dsl._

  val creditCardRoutes = HttpRoutes.of[F] {
    case req@POST -> Root / "creditcards" =>
      for {
        userinfo <- req.as[UserInformation].adaptError {
          case NonFatal(err: InvalidMessageBodyFailure) => RequestError(err.getMessage)
          case NonFatal(err: MalformedMessageBodyFailure) => RequestError(err.getMessage)
        }
        cSCards <- creditCardService.action(userinfo)
        resp <- Ok(cSCards)
      } yield resp
  }

  val route: HttpRoutes[F] = creditCardRoutes
}
