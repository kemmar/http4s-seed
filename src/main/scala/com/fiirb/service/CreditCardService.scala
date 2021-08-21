package com.fiirb.service

import cats.effect.ConcurrentEffect
import cats.implicits._
import com.fiirb.domain.cscard.CSResult
import com.fiirb.domain.scoredcard.ScoredCardResult
import com.fiirb.domain.user.{UserInformation, UserResult}

class CreditCardService[F[_] : ConcurrentEffect](cSCardsService: CSCardsService[F],
                                                 scoredCardsService: ScoredCardsService[F]) {

  def action(userInformation: UserInformation): F[List[UserResult]] = {
    for {
      cSResp <- cSCardsService.getCreditCards(userInformation)
      scoredResp <- scoredCardsService.getCreditCards(userInformation)
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
