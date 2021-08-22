package com.fiirb.service

import com.fiirb.domain.NovelInfo
import com.fiirb.endpoint.ReadNovelsHomeEndpoint

class WuxiaService[F[_]](ReadNovelsHomeEndpoint: ReadNovelsHomeEndpoint[F]) {

  def listNovels(): F[List[NovelInfo]] = ReadNovelsHomeEndpoint.getAllNovelsOnHomepage()

  def findNovels(search: String): F[List[NovelInfo]] = ???

}
