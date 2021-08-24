package com.fiirb.service

import cats.effect.Sync
import cats.implicits._
import com.fiirb.domain.{Chapter, ChapterInfo, NovelInfo}
import com.fiirb.endpoint.{ReadNovelsChapterEndpoint, ReadNovelsChapterListEndpoint, ReadNovelsHomeEndpoint, ReadNovelsSearchEndpoint}

import java.io.File

class WuxiaService[F[_] : Sync](ReadNovelsHomeEndpoint: ReadNovelsHomeEndpoint[F],
                         readNovelsSearchEndpoint: ReadNovelsSearchEndpoint[F],
                         readNovelsChapterListEndpoint: ReadNovelsChapterListEndpoint[F],
                         readNovelsChapterEndpoint: ReadNovelsChapterEndpoint[F]
                        ) {

  def download(name: String): F[List[Chapter]] = {
    for {
      info <- listNovelChapters(name)
      chapters <- info.chapters.map { chapter =>
       readNovelChapter(name, chapter.urlPath)
      }.sequence
    } yield chapters
  }

  def listNovels(): F[List[NovelInfo]] = ReadNovelsHomeEndpoint.getAllNovelsOnHomepage()

  def findNovels(search: String): F[List[NovelInfo]] = readNovelsSearchEndpoint.getAllNovelsOnHomepage(search)

  def listNovelChapters(novelName: String): F[ChapterInfo] = readNovelsChapterListEndpoint.listNovelChapters(novelName)

  def readNovelChapter(novelName: String, chapterName: String): F[Chapter] = readNovelsChapterEndpoint.readNovelChapter(novelName, chapterName)

}
