package com.fiirb.endpoint

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.NovelInfo
import com.fiirb.util.AppConfig
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import scala.jdk.CollectionConverters._
import scala.util.Try

class ReadNovelsHomeEndpoint[F[_] : Sync]() {

  private def getHomePageElements: F[Elements] = {
    Sync[F]
      .fromTry(Try(Jsoup.connect(AppConfig.novelServiceBaseUrl).get.select(".section-item a")))
  }

  private def formatNovels(elements: Elements): F[List[NovelInfo]] =
    Sync[F]
      .fromTry {
        elements.asScala.map { headline =>
          Try {
            val link: String =
              headline
                .select(".item-img").attr("href").replace("/","")
            val name: String = headline.select(".img").attr("alt")
            val image: String = headline.select(".img").attr("src")

            NovelInfo(
              name,
              image,
              link
            )
          }
        }.toList.sequence.map(_.filterNot(novel => novel.name.trim.isEmpty || novel.uri.trim.isEmpty))
      }

  def getAllNovelsOnHomepage(): F[List[NovelInfo]] = for {
    elms <- getHomePageElements
    novels <- formatNovels(elms)
  } yield novels
}
