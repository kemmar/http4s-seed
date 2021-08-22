package com.fiirb.util

import com.fiirb.error.Errors.ServiceError
import org.http4s._
import org.http4s.client.Client

import scala.util.control.NonFatal

trait HttpClientHelpers[F[_]] {
  def C: Client[F]

  def endpointName: String

  def postRequest[A, B](endpoint: Uri, reqBody: A)
                       (implicit ee: EntityEncoder[F, A],
                        ed: EntityDecoder[F, B]): F[B] = {
    C.expect[B](
      Request[F](
        Method.POST,
        endpoint
      ).withEntity(reqBody))
  }

  def baseServiceErrorHandler: PartialFunction[Throwable, Throwable] = {
    case NonFatal(_: Throwable) => ServiceError(endpointName, "Failed to communicate with service")
  }
}
