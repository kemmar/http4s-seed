package com.fiirb.controller

import cats.effect.Sync
import cats.implicits._
import com.fiirb.Params.SearchQueryParamMatcher
import com.fiirb.service.WuxiaService
import com.fiirb.util.BaseController
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class WuxiaController[F[_]: Sync](wuxiaService: WuxiaService[F])(implicit dsl: Http4sDsl[F]) extends BaseController[F] {

  import dsl._

  val searchNovelsRoutes = HttpRoutes.of[F] {
    case GET -> Root / "novel" :? SearchQueryParamMatcher(search) =>
      for {
        novelList <- wuxiaService.findNovels(search)
        resp <- Ok(novelList)
      } yield resp

    case GET -> Root / "novel" =>
      for {
        novelList <- wuxiaService.listNovels()
        resp <- Ok(novelList)
      } yield resp

    case GET -> Root / "novel" / name =>
      for {
        novelInformation <- wuxiaService.listNovelChapters(name)
        resp <- Ok(novelInformation)
      } yield resp

    case POST -> Root / "novel" / name =>
      for {
        novelInformation <- wuxiaService.download(name)
        resp <- Ok(novelInformation)
      } yield resp

    case GET -> Root / "novel" / name / chapter =>
      for {
        novelChapter <- wuxiaService.readNovelChapter(name, chapter)
        resp <- Ok(novelChapter)
      } yield resp
  }

  val route: HttpRoutes[F] = searchNovelsRoutes
}
