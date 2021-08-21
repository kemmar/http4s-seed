package com.fiirb.service

import cats.effect.IO
import com.fiirb.domain.scoredcard.ScoredCardResult
import munit.CatsEffectSuite
import org.http4s.client.UnexpectedStatus
import org.http4s.{Status, Uri}
import com.fiirb.util.TestData.USER_INFORMATION
import com.fiirb.util.TestHttpClient

class ScoredCardsServiceTest extends CatsEffectSuite with TestHttpClient {
  private val uri = Uri.uri("https://app.clearscore.com/api/global/backend-tech-test/v2/creditcards")

  test("can handle successful requests") {
    val client = httpClient(
      uri = uri,
      respBody =
        """[{"card": "SuperSaver Card",
          |"apr": 123,
          |"approvalRating": 5.5
          |}]""".stripMargin
    )

    val service = new ScoredCardsService[IO](client)

    assertIO(service.getCreditCards(USER_INFORMATION),
      List(ScoredCardResult("SuperSaver Card", 123, 5.5
      )))
  }

  test("can handle failed 4** requests") {

    val client = httpClient(
      uri = uri,
      respBody =
        """The request content was malformed:
          |Expected String as JsString, but got 5""".stripMargin, status = Status.BadRequest)

    val service = new ScoredCardsService[IO](client)

    interceptIO[UnexpectedStatus](service.getCreditCards(USER_INFORMATION))
  }

  test("can handle failed 5** requests") {

    val client =
      httpClient(
        uri = uri,
        status = Status.ServiceUnavailable)

    val service = new ScoredCardsService[IO](client)

    interceptIO[UnexpectedStatus](service.getCreditCards(USER_INFORMATION))
  }

}
