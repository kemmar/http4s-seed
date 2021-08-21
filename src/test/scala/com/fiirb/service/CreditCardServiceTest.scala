package com.fiirb.service

import cats.effect.IO
import cats.implicits._
import com.fiirb.domain.cscard.CSResult
import com.fiirb.domain.scoredcard.ScoredCardResult
import com.fiirb.domain.user.UserResult
import com.fiirb.util.TestData.USER_INFORMATION
import munit.CatsEffectSuite
import org.http4s.client.Client
import org.mockito.MockitoSugar

class CreditCardServiceTest extends CatsEffectSuite with MockitoSugar {
  val mockScoredCardsService = mock[ScoredCardsService[IO]]
  val mockCSCardsService = mock[CSCardsService[IO]]

  val service = new CreditCardService[IO](mockCSCardsService, mockScoredCardsService)

  test("tests correct service calls are being made and result is ordered correctly") {

    val scoredCardResponses = List(
      ScoredCardResult("ScoredCard Builder", 19.4, 0.8)
    )

    val clearScoreResponses = List(
      CSResult("SuperSpender Card", 19.2, 5.0),
      CSResult("SuperSaver Card", 21.4, 6.3)
    )

    val userResults = List(
      UserResult("ScoredCards", "ScoredCard Builder", 19.4, BigDecimal(0.212)),
      UserResult("CSCards", "SuperSaver Card", 21.4, BigDecimal(0.137)),
      UserResult("CSCards", "SuperSpender Card", 19.2, BigDecimal(0.135))
    )

    when(mockScoredCardsService.getCreditCards(USER_INFORMATION))
      .thenReturn(scoredCardResponses.pure[IO])

    when(mockCSCardsService.getCreditCards(USER_INFORMATION))
      .thenReturn(clearScoreResponses.pure[IO])

    // tested and this cares about order
    assertIO(service.action(USER_INFORMATION), userResults)
  }

}
