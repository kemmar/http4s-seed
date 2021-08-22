package com.fiirb.controller

import cats.effect.IO
import cats.implicits._
import com.fiirb.domain.user.UserResult
import com.fiirb.error.Errors.{RequestError, ServiceError}
import com.fiirb.service.CreditCardService
import com.fiirb.util.TestData.USER_INFORMATION
import io.circe.Json
import io.circe.literal._
import io.circe.syntax._
import munit.CatsEffectSuite
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.{Method, Request}
import org.mockito.MockitoSugar

class CreditCardControllerTest extends CatsEffectSuite with MockitoSugar {

  implicit lazy val dsl = new Http4sDsl[IO] {}

  private lazy val service: CreditCardService[IO] = mock[CreditCardService[IO]]
  private lazy val controller = new CreditCardController[IO](service)

  val body = json"""{"name": "John Smith", "creditScore": 123, "salary": 123}"""

  test("returns successfully") {

    val request = Request[IO](Method.POST, uri"/creditcards").withEntity(body)

    val testResult = List(UserResult("hello", "world", 123.0, 123.0))

    when(service.action(USER_INFORMATION)) thenReturn (testResult.pure[IO])

    val result = controller.route.orNotFound(request)

    assertIO(result.flatMap(_.as[Json]), testResult.asJson)
  }

  test("returns successfully ordered by card score") {

    val request = Request[IO](Method.POST, uri"/creditcards").withEntity(body)

    val testResult = List(UserResult("hello", "world", 123.0, 123.0))

    when(service.action(USER_INFORMATION)) thenReturn (testResult.pure[IO])

    val result = controller.route.orNotFound(request)

    assertIO(result.flatMap(_.as[Json]), testResult.asJson)
  }

  test("handles service errors") {

    val request = Request[IO](Method.POST, uri"/creditcards").withEntity(body)

    when(service.action(USER_INFORMATION)) thenReturn (IO.raiseError(ServiceError("Some Service", "some err")))

    val result = controller.route.orNotFound(request)

    interceptIO[ServiceError](result)
  }

  test("handles json errors") {

    val body = json"""{"fail":"me"}"""

    val request = Request[IO](Method.POST, uri"/creditcards").withEntity(body)

    when(service.action(USER_INFORMATION))

    val result = controller.route.orNotFound(request)

    interceptIO[RequestError](result)
  }

}
