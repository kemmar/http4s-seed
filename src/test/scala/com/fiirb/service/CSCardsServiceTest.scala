package com.fiirb.service

import cats.effect.IO
import com.fiirb.domain.cscard.CSResult
import com.fiirb.util.TestData.USER_INFORMATION
import com.fiirb.util.TestHttpClient
import munit.CatsEffectSuite
import org.http4s.client.UnexpectedStatus
import org.http4s.{Status, Uri}

class CSCardsServiceTest extends CatsEffectSuite with TestHttpClient {

  private val uri = Uri.uri("https://app.clearscore.com/api/global/backend-tech-test/v1/cards")

  test("can handle successful requests") {
    val client = httpClient(
      uri = uri,
      respBody = """[{"cardName":"Clear Score Card Builder","eligibility":5.5,"apr":19.4}]""")

    val service = new CSCardsService[IO](client)

    assertIO(service.getCreditCards(USER_INFORMATION),
      List(CSResult("Clear Score Card Builder", 19.4, 5.5
      )))
  }

  test("can handle failed 4** requests") {

    val client = httpClient(
      uri = uri,
      respBody =
        """The request content was malformed:
          |Expected String as JsString, but got 5""".stripMargin, status = Status.BadRequest)

    val service = new CSCardsService[IO](client)

    interceptIO[UnexpectedStatus](service.getCreditCards(USER_INFORMATION))
  }

  test("can handle failed 5** requests") {

    val client = httpClient(uri = uri, status = Status.ServiceUnavailable)

    val service = new CSCardsService[IO](client)

    interceptIO[UnexpectedStatus](service.getCreditCards(USER_INFORMATION))
  }

}
