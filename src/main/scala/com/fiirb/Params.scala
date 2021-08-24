package com.fiirb

import org.http4s.dsl.io.QueryParamDecoderMatcher

object Params {

  object SearchQueryParamMatcher extends QueryParamDecoderMatcher[String]("search")

}
