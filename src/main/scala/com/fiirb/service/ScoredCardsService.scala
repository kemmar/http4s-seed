package com.fiirb.service

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.scoredcard.{ScoredCardResult, ScoredCardsRequest}
import com.fiirb.domain.user.UserInformation
import com.fiirb.util.{AppConfig, HttpClientHelpers, ServiceBase}
import org.http4s.client.Client

class ScoredCardsService[F[_] : Sync](override val C: Client[F]) extends HttpClientHelpers[F] with ServiceBase[F, ScoredCardResult] {

  val endpointUri: String = s"${AppConfig.scoredCardsBaseUrl}/api/global/backend-tech-test/v2/creditcards"

  def getCreditCards(userInformation: UserInformation): F[List[ScoredCardResult]] = {
    for {
      reqBody <- userInformation.toScoredCardsRequest[F]
      response <- postRequest[ScoredCardsRequest, List[ScoredCardResult]](endpoint, reqBody)
    } yield response
  }

}