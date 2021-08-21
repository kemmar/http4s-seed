package com.fiirb.controller

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.user.UserInformation
import com.fiirb.service.CreditCardService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class CreditCardController[F[_] : Sync](creditCardService: CreditCardService[F])(implicit dsl: Http4sDsl[F]) {

  import dsl._

  val creditCardRoutes = HttpRoutes.of[F] {
    case req@POST -> Root / "creditcards" =>
      for {
        userinfo <- req.as[UserInformation]
        cSCards <- creditCardService.action(userinfo)
        resp <- Ok(cSCards)
      } yield resp
  }

  val route: HttpRoutes[F] = creditCardRoutes
}
