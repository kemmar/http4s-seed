package com.fiirb.service

import cats.effect.ConcurrentEffect
import cats.implicits._
import com.fiirb.domain.cscard.CSResult
import com.fiirb.domain.scoredcard.ScoredCardResult
import com.fiirb.domain.user.{UserInformation, UserResult}
import com.fiirb.endpoint.{CSCardsEndpoint, ScoredCardsEndpoint}

class CreditCardService[F[_] : ConcurrentEffect](cSCardsEndpoint: CSCardsEndpoint[F],
                                                 scoredCardsEndpoint: ScoredCardsEndpoint[F]) {

  def action(userInformation: UserInformation): F[List[UserResult]] = {
    for {
      cSResp <- cSCardsEndpoint.getCreditCards(userInformation)
      scoredResp <- scoredCardsEndpoint.getCreditCards(userInformation)
      result <- aggregateAndSort(cSResp, scoredResp)
    } yield result
  }

  private[service] def aggregateAndSort(cSResp: List[CSResult],
                                        scoredResp: List[ScoredCardResult]): F[List[UserResult]] = {
    (cSResp.map(_.toUserResult) ++ scoredResp.map(_.toUserResult))
      .sortBy(res => (res.cardScore, res.apr))(Ordering[(BigDecimal, Double)].reverse)
      .pure[F]
  }
}
