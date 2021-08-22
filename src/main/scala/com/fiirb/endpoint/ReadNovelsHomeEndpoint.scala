package com.fiirb.endpoint

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.NovelInfo
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import scala.jdk.CollectionConverters._
import scala.util.Try

class ReadNovelsHomeEndpoint[F[_] : Sync]() {

  private def getHomePageElements: F[Elements] = {
    Sync[F]
      .fromTry(Try(Jsoup.connect("https://www.readlightnovel.cc/").get.select(".section-item a")))
  }

  def getAllNovelsOnHomepage(): F[List[NovelInfo]] = {
    getHomePageElements.map { elms =>
      elms.asScala.flatMap { headline =>
        Try {
          val link: String = headline.attr("class", "item-img").attr("href")
          val name: String = headline.select(".img").attr("alt")
          val image: String = headline.select(".img").attr("src")

          NovelInfo(
            name,
            image,
            link
          )
        }.toOption
      }.toList.filterNot(novel => novel.name.trim.isEmpty || novel.uri.trim.isEmpty)
    }
  }
}
