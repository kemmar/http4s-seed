package com.fiirb.domain

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class ChapterElm(name: String, urlPath: String)

object ChapterElm {
  implicit val chapterElmEncoder: Encoder[ChapterElm] = deriveEncoder[ChapterElm]

  implicit def chapterElmEntityEncoder[F[_]]: EntityEncoder[F, ChapterElm] =
    jsonEncoderOf
}

case class ChapterInfo(name: String, imgUrl: String, chapters: List[ChapterElm] = List.empty) {
  def withChapters(chapters: List[ChapterElm]): ChapterInfo = this.copy(chapters = chapters)
}

object ChapterInfo {
  implicit val chapterInfoEncoder: Encoder[ChapterInfo] = deriveEncoder[ChapterInfo]

  implicit def chapterInfoEntityEncoder[F[_]]: EntityEncoder[F, ChapterInfo] =
    jsonEncoderOf
}
