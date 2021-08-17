package com.fiirb.controller

import cats.effect.IO
import munit.CatsEffectSuite
import org.http4s.client.dsl.io._
import org.http4s.implicits._
import io.circe.literal._
import org.http4s.Method.POST
import org.http4s.{Response, Status}
import org.http4s.circe.CirceEntityEncoder._

import scala.concurrent.ExecutionContext.Implicits.global

class CreditCardControllerTest extends CatsEffectSuite {

  private[this] val returnCreditCards: IO[Response[IO]] = {
    val listCreditCards = POST(json"""{"name":"Brian Lewis", "creditScore": 123, "salary": 123}""", uri"/creditcards")
    new CreditCardController().route.orNotFound(listCreditCards)
  }

  test("HelloWorld returns status code 200") {
    assertIO(returnCreditCards.map(_.status) ,Status.Ok)
  }
}
