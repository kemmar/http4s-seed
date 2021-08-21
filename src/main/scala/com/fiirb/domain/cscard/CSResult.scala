package com.fiirb.domain.cscard

import cats.effect.Sync
import com.fiirb.domain.user.UserResult
import com.fiirb.util.CardScoreUtil
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

case class CSResult(cardName: String, apr: Double, eligibility: Double) extends CardScoreUtil {
  // multiply by 10 to make the eligibility up to 100
  def toUserResult =
    UserResult(
      provider = "CSCards",
      name = cardName,
      apr = apr,
      cardScore = createCardScore(eligibility * 10))
}

object CSResult {
  implicit val cSCardDecoder: Decoder[CSResult] = deriveDecoder[CSResult]

  implicit def cSCardEntityDecoder[F[_] : Sync]: EntityDecoder[F, List[CSResult]] =
    jsonOf

  implicit val cSCardEncoder: Encoder[CSResult] = deriveEncoder[CSResult]

  implicit def cSCardEntityEncoder[F[_]]: EntityEncoder[F, List[CSResult]] =
    jsonEncoderOf
}