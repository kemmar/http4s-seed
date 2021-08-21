package com.fiirb.domain

import cats.effect.IO
import com.fiirb.domain.user.UserInformation
import io.circe.Decoder.Result
import io.circe.literal._
import io.circe.syntax._
import munit.CatsEffectSuite

import scala.util.Random

class UserInformationTest extends CatsEffectSuite {

  def userInformation(creditScore: Int = Random.between(0, 700),
                      salary: Int = Random.between(0, 10_000_000)): Result[UserInformation] = {
    json"""{"name": "John Smith", "creditScore": $creditScore, "salary": $salary}""".as[UserInformation]
  }

  test("rejects requests where user's salary is less than 0") {
    val err = intercept[IllegalArgumentException] {
      userInformation(salary = -Random.between(1, Int.MaxValue))
    }

    assertEquals(err.getMessage, "requirement failed: invalid salary")
  }

  test("rejects requests where user's credit score is less than 0") {

    val negativeScore = -Random.between(1, 700)

    val err = intercept[IllegalArgumentException] {
      userInformation(creditScore = negativeScore)
    }

    assertEquals(err.getMessage, "requirement failed: invalid credit score")
  }

  test("rejects requests where user's credit score is greater than 700") {

    val superHighScore = Random.between(700, Int.MaxValue)

    val err = intercept[IllegalArgumentException] {
      userInformation(creditScore = superHighScore, salary = Int.MaxValue)
    }

    assertEquals(err.getMessage, "requirement failed: invalid credit score")
  }

  test("can be converted to clear score request json") {

    val userInformation = UserInformation("Brian Lewis", 500, Int.MaxValue)

    val clearScoreRequest = userInformation.toCSRequest[IO].map(_.asJson)

    assertIO(clearScoreRequest, json"""{"name": "Brian Lewis", "creditScore": 500}""")
  }

  test("can be converted to scored request json") {

    val userInformation = UserInformation("Brian Lewis", 500, 1)

    val clearScoreRequest = userInformation.toScoredCardsRequest[IO].map(_.asJson)

    assertIO(clearScoreRequest, json"""{"name": "Brian Lewis", "score": 500, "salary": 1}""")
  }

}
