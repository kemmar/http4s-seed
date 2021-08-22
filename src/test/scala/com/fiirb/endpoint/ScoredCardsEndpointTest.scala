package com.fiirb.endpoint

import cats.effect.IO
import com.fiirb.domain.scoredcard.ScoredCardResult
import com.fiirb.error.Errors.ServiceError
import com.fiirb.util.TestData.USER_INFORMATION
import com.fiirb.util.TestHttpClient
import munit.CatsEffectSuite
import org.http4s.{Status, Uri}

class ScoredCardsEndpointTest extends CatsEffectSuite with TestHttpClient {
  private val uri = Uri.uri("https://test.com/api/global/backend-tech-test/v2/creditcards")

  test("can handle successful requests") {
    val client = httpClient(
      uri = uri,
      respBody =
        """[{"card": "SuperSaver Card",
          |"apr": 123,
          |"approvalRating": 5.5
          |}]""".stripMargin
    )

    val service = new ScoredCardsEndpoint[IO](client)

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

    val service = new ScoredCardsEndpoint[IO](client)

    interceptIO[ServiceError](service.getCreditCards(USER_INFORMATION))
  }

  test("can handle failed 5** requests") {

    val client =
      httpClient(
        uri = uri,
        status = Status.ServiceUnavailable)

    val service = new ScoredCardsEndpoint[IO](client)

    interceptIO[ServiceError](service.getCreditCards(USER_INFORMATION))
  }

}