package com.fiirb.endpoint

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.{ChapterElm, ChapterInfo, NovelInfo}
import com.fiirb.util.{AppConfig, EncryptionUtil}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.jdk.CollectionConverters._
import scala.util.Try

class ReadNovelsChapterListEndpoint[F[_] : Sync]() {

  private def getChapterInfoElements(novelName: String): F[Document] = {
    Sync[F]
      .fromTry(Try(Jsoup.connect(s"${AppConfig.novelServiceBaseUrl}/$novelName/").get))
  }

  private def formatChapterInfo(doc: Document): F[ChapterInfo] = {
    val chapterInfo: F[Option[ChapterInfo]] = Sync[F]
      .fromTry {
        doc.select(".book-wrapper").asScala.map { headline =>
          Try {
            val imgLing: String = headline.select("div.book-img > img").attr("src")
            val name: String = headline.select(".book-name").text()

            ChapterInfo(name, imgLing)
          }
        }.toList
          .sequence
          .map(_.filterNot(chapterInfo => chapterInfo.name.trim.isEmpty || chapterInfo.imgUrl.trim.isEmpty).headOption)
      }

    for {
      info <- chapterInfo
      result <- if(info.isEmpty) Sync[F].raiseError(new Exception("err")) else info.get.pure[F]
    } yield result
  }

  private def formatNovelChapter(doc: Document, novelName: String): F[List[ChapterElm]] =
    Sync[F]
      .fromTry {
       doc.select(".chapter-list a").asScala.map { headline =>
          Try {
            val link: String = headline.select(".chapter-item").attr("href").drop(1)
            val name: String = headline.select("a.chapter-item > div.chapter-info > p.chapter-name").text()

            EncryptionUtil.encrypt(link).map(encrLink => ChapterElm(name, encrLink))
          }
        }.toList
         .sequence
         .map(_.sequence.map(_.filterNot(chapter => chapter.name.trim.isEmpty || chapter.urlPath.trim.isEmpty)))
      }.flatten

  def listNovelChapters(novelName: String): F[ChapterInfo] = for {
    doc <- getChapterInfoElements(novelName)
    chapterInfo <- formatChapterInfo(doc)
    chapters <- formatNovelChapter(doc, novelName)
  } yield chapterInfo.withChapters(chapters)
}
