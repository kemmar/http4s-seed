package com.fiirb.domain.user

import cats.effect.Sync
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

case class UserResult(provider: String, name: String, apr: Double, cardScore: BigDecimal)

object UserResult {
  implicit val userResultDecoder: Decoder[UserResult] = deriveDecoder[UserResult]

  implicit def userResultEntityDecoder[F[_] : Sync]: EntityDecoder[F, List[UserResult]] =
    jsonOf

  implicit val userResultEncoder: Encoder[UserResult] = deriveEncoder[UserResult]

  implicit def userResultEntityEncoder[F[_]]: EntityEncoder[F, List[UserResult]] =
    jsonEncoderOf
}
