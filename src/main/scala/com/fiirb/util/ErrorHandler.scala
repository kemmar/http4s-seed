package com.fiirb.util

import cats.Applicative
import com.fiirb.error.Errors.{ApplicationError, RequestError, ServiceError, UriError}
import org.http4s.server.ServiceErrorHandler
import org.http4s.dsl.Http4sDsl

class ErrorHandler[F[_] : Applicative]()(implicit dsl: Http4sDsl[F]) {

  import dsl._

  val errorHandler: ServiceErrorHandler[F] = { request => {
    case err: RequestError => BadRequest(ApplicationError(request.uri.path, err))
    case err: ServiceError => UnprocessableEntity(ApplicationError(request.uri.path, err))
    case err: UriError => InternalServerError(ApplicationError(request.uri.path, err))
    case err => InternalServerError(ApplicationError(request.uri.path, "System Error", err.getMessage))
  }
  }

}
