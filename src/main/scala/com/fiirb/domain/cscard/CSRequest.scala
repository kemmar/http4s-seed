package com.fiirb.domain.cscard

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class CSRequest(name: String, creditScore: Int)

object CSRequest {
  implicit val cSRequestDecoder: Decoder[CSRequest] = deriveDecoder[CSRequest]

  implicit def cSRequestEntityDecoder[F[_] : Sync]: EntityDecoder[F, CSRequest] =
    jsonOf

  implicit val cSRequestEncoder: Encoder[CSRequest] = deriveEncoder[CSRequest]

  implicit def cSRequestEntityEncoder[F[_]]: EntityEncoder[F, CSRequest] =
    jsonEncoderOf
}
