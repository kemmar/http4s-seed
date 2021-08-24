package com.fiirb.util

import cats.effect.Sync
import org.http4s.Uri
import cats.implicits._
import com.fiirb.error.Errors.UriError

import scala.util.control.NonFatal

abstract class ServiceBase[F[_] : Sync, T] {


  def endpointUri: String

  def endpointName: String

  lazy val endpoint: F[Uri] =
    Sync[F]
      .fromEither(Uri.fromString(endpointUri))
      .adaptError {
    case NonFatal(err: Throwable) => UriError(endpointUri, err.getMessage)
  }
}
