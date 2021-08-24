package com.fiirb.endpoint

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.NovelInfo
import com.fiirb.util.AppConfig
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.jdk.CollectionConverters._
import scala.util.Try

class ReadNovelsSearchEndpoint[F[_] : Sync]() {

  private def searchPageElements(keyword: String, page: Int): F[Document] = {
    Sync[F]
      .fromTry(Try(Jsoup.connect(s"${AppConfig.novelServiceBaseUrl}/search/$keyword/$page").get))
  }

  private def toNovelInfo(doc: Document): F[List[NovelInfo]] = Sync[F].fromTry {
    doc.select(".list-item a").asScala.toList.map { headline =>
      Try {
        val link: String = headline.attr("class", "item-img").attr("href")
        val name: String = headline.select(".item-img").attr("alt")
        val image: String = headline.select(".item-img").attr("src")

        NovelInfo(
          name,
          image,
          link
        )
      }
    }.sequence.map(_.filterNot(novel => novel.name.trim.isEmpty || novel.uri.trim.isEmpty))
  }

  def findPages(keyword: String, doc: Document, startingPage: Int): F[List[NovelInfo]] = for {
    pages <- Sync[F].fromTry{ doc.select(".pages a").asScala.map { headline =>
      Try {
        val pageCount = headline.text().toInt
        pageCount
      }
    }.toList.sequence.map(_.dropWhile(_ == startingPage))}
    novels <- pages.map(mergePagination(keyword, _)).sequence
  } yield novels.flatten

  private def mergePagination(keyword: String, page: Int): F[List[NovelInfo]] = for {
    page <- searchPageElements(keyword, page)
    novels <- toNovelInfo(page)
  } yield novels

  def getAllNovelsOnHomepage(keyword: String, page: Int = 1): F[List[NovelInfo]] = for {
    firstPage <- searchPageElements(keyword, page)
    allPages <- findPages(keyword, firstPage, page)
  } yield allPages
}
