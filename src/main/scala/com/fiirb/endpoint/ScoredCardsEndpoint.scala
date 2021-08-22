package com.fiirb.endpoint

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.scoredcard.{ScoredCardResult, ScoredCardsRequest}
import com.fiirb.domain.user.UserInformation
import com.fiirb.util.{AppConfig, HttpClientHelpers, ServiceBase}
import org.http4s.client.Client

class ScoredCardsEndpoint[F[_] : Sync](override val C: Client[F]) extends ServiceBase[F, ScoredCardResult] with HttpClientHelpers[F] {

  val endpointName: String = "Scored Cards"

  val endpointUri: String = s"${AppConfig.scoredCardsBaseUrl}/api/global/backend-tech-test/v2/creditcards"

  def getCreditCards(userInformation: UserInformation): F[List[ScoredCardResult]] = {
    for {
      reqBody <- userInformation.toScoredCardsRequest[F]
      url <- endpoint
      response <- postRequest[ScoredCardsRequest, List[ScoredCardResult]](url, reqBody)
        .adaptError(baseServiceErrorHandler)
    } yield response
  }

}