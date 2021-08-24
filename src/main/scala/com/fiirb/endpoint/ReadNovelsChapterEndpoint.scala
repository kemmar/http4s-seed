package com.fiirb.endpoint

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.{Chapter, ChapterInfo, NovelInfo}
import com.fiirb.util.{AppConfig, EncryptionUtil}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.jdk.CollectionConverters._
import scala.util.Try

class ReadNovelsChapterEndpoint[F[_] : Sync]() {

  private def getChapterElements(chapterName: String): F[Document] = for {
    name <- EncryptionUtil.decrypt(chapterName)
    document <- Sync[F]
      .fromTry(Try(Jsoup.connect(s"${AppConfig.novelServiceBaseUrl}/$name").get))
  } yield document

  private def formatChapter(doc: Document, novelName: String): F[Chapter] =
    Sync[F]
      .fromTry {
        Try {
          val title = doc.select(".chapter-title").text()
          val chapter = doc.select(".chapter-entity").text()

          for {
            nextPage <- EncryptionUtil.encrypt(doc.select(".next").attr("href").drop(1))
            prevPage <- EncryptionUtil.encrypt(doc.select(".prev").attr("href").drop(1))
          } yield Chapter(
            title,
            chapter,
            nextPage,
            prevPage
          )
        }
      }.flatten

  def readNovelChapter(novelName: String, chapterName: String): F[Chapter] = for {
    doc <- getChapterElements(chapterName)
    chapter <- formatChapter(doc, novelName)
  } yield chapter
}
