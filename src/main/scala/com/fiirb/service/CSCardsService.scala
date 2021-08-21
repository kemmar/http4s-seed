package com.fiirb.service

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.user.UserInformation
import com.fiirb.domain.cscard.{CSRequest, CSResult}
import com.fiirb.util.{AppConfig, HttpClientHelpers, ServiceBase}
import org.http4s.client.Client

class CSCardsService[F[_] : Sync](override val C: Client[F]) extends HttpClientHelpers[F] with ServiceBase[F, CSResult] {

  val endpointUri: String = s"${AppConfig.scoredCardsBaseUrl}/api/global/backend-tech-test/v1/cards"

  def getCreditCards(userInformation: UserInformation): F[List[CSResult]] = {
    for {
      reqBody <- userInformation.toCSRequest[F]
      response <- postRequest[CSRequest, List[CSResult]](endpoint, reqBody)
    } yield response

  }

}