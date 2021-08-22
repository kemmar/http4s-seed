package com.fiirb.domain

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class NovelInfo(name: String, imgUri: String, uri: String)

object NovelInfo {
  implicit val novelInfoEncoder: Encoder[NovelInfo] = deriveEncoder[NovelInfo]

  implicit def novelInfoEntityEncoder[F[_]]: EntityEncoder[F, List[NovelInfo]] =
    jsonEncoderOf
}
