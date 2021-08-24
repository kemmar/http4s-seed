package com.fiirb.domain

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class Chapter(title: String,
                   chapter: String,
                   nextPage: String,
                   prevPage: String)

object Chapter {
  implicit val chapterEncoder: Encoder[Chapter] = deriveEncoder[Chapter]

  implicit def chapterEntityEncoder[F[_]]: EntityEncoder[F, Chapter] =
    jsonEncoderOf

  implicit def chapterListEntityEncoder[F[_]]: EntityEncoder[F, List[Chapter]] =
    jsonEncoderOf
}
