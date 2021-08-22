package com.fiirb.endpoint

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.cscard.{CSRequest, CSResult}
import com.fiirb.domain.user.UserInformation
import com.fiirb.util.{AppConfig, HttpClientHelpers, ServiceBase}
import org.http4s.client.Client

class CSCardsEndpoint[F[_] : Sync](override val C: Client[F]) extends ServiceBase[F, CSResult] with HttpClientHelpers[F] {

  val endpointName: String = "Clear Score"
  val endpointUri: String = s"${AppConfig.csCardsBaseUrl}/api/global/backend-tech-test/v1/cards"

  def getCreditCards(userInformation: UserInformation): F[List[CSResult]] = {
    for {
      reqBody <- userInformation.toCSRequest[F]
      url <- endpoint
      response <- postRequest[CSRequest, List[CSResult]](url, reqBody)
        .adaptError(baseServiceErrorHandler)
    } yield response

  }

}