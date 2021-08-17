package com.fiirb.domain

import io.circe.Decoder.Result
import io.circe.generic.auto._
import io.circe.literal._
import munit.FunSuite

import scala.util.Random

class UserInformationTest extends FunSuite {

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

    val err = intercept[IllegalArgumentException]{
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

}
