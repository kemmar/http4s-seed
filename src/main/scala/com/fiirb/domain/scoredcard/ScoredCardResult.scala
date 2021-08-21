package com.fiirb.domain.scoredcard

import cats.effect.Sync
import com.fiirb.domain.user.UserResult
import com.fiirb.util.CardScoreUtil
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

case class ScoredCardResult(card: String, apr: Double, approvalRating: Double) extends CardScoreUtil {
  // multiply by 100 to make the eligibility up to 100
  def toUserResult =
    UserResult(
      provider = "ScoredCards",
      name = card,
      apr = apr,
      cardScore = createCardScore(approvalRating * 100))
}

object ScoredCardResult {
  implicit val scoredCardResultDecoder: Decoder[ScoredCardResult] = deriveDecoder[ScoredCardResult]

  implicit def scoredCardResultEntityDecoder[F[_] : Sync]: EntityDecoder[F, List[ScoredCardResult]] =
    jsonOf

  implicit val scoredCardResultEncoder: Encoder[ScoredCardResult] = deriveEncoder[ScoredCardResult]

  implicit def scoredCardResultEntityEncoder[F[_]]: EntityEncoder[F, ScoredCardResult] =
    jsonEncoderOf
}

