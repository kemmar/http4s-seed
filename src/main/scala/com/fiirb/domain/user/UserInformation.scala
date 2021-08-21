package com.fiirb.domain.user

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.cscard.CSRequest
import com.fiirb.domain.scoredcard.ScoredCardsRequest
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

case class UserInformation(name: String, creditScore: Int, salary: Int) {

  require(creditScore <= 700 && creditScore >= 0, "invalid credit score")

  require(salary >= 0, "invalid salary")

  def toCSRequest[F[_] : Applicative]: F[CSRequest] = CSRequest(name, creditScore).pure[F]

  def toScoredCardsRequest[F[_] : Applicative]: F[ScoredCardsRequest] = ScoredCardsRequest(name, creditScore, salary).pure[F]
}

object UserInformation {
  implicit val userInformationDecoder: Decoder[UserInformation] = deriveDecoder[UserInformation]

  implicit def userInformationEntityDecoder[F[_] : Sync]: EntityDecoder[F, UserInformation] =
    jsonOf

  implicit val userInformationEncoder: Encoder[UserInformation] = deriveEncoder[UserInformation]

  implicit def userInformationEntityEncoder[F[_]]: EntityEncoder[F, UserInformation] =
    jsonEncoderOf
}
