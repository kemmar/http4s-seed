package com.fiirb.domain.scoredcard

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class ScoredCardsRequest(name: String, score: Int, salary: Int)

object ScoredCardsRequest {
  implicit val scoredCardsRequestEncoder: Encoder[ScoredCardsRequest] = deriveEncoder[ScoredCardsRequest]

  implicit def scoredCardsRequestEntityEncoder[F[_]]: EntityEncoder[F, ScoredCardsRequest] =
    jsonEncoderOf
}
