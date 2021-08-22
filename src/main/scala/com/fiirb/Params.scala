package com.fiirb

import org.http4s.QueryParamDecoder
import org.http4s.dsl.io.QueryParamDecoderMatcher

object Params {

  private implicit val stringParamDecoder: QueryParamDecoder[String] =
    QueryParamDecoder[String]

  object SearchQueryParamMatcher extends QueryParamDecoderMatcher[String]("search")


}
