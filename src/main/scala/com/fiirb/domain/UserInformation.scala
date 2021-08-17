package com.fiirb.domain

import cats.effect.IO
import org.http4s.circe.jsonOf
import io.circe.generic.auto._

case class UserInformation(name: String, creditScore: Int, salary: Int) {

  require(creditScore <= 700 && creditScore >= 0, "invalid credit score")

  require(salary >= 0, "invalid salary")

}

object UserInformation {
  implicit val decoder = jsonOf[IO, UserInformation]
}
