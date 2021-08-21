package com.fiirb.util

import org.http4s.client.Client
import org.http4s._

trait HttpClientHelpers[F[_]] {

  def C: Client[F]

  def postRequest[A, B](endpoint: Uri, reqBody: A)
                       (implicit ee: EntityEncoder[F, A],
                        ed: EntityDecoder[F, B]): F[B] = {
    C.expect[B](
      Request[F](
        Method.POST,
        endpoint
      ).withEntity(reqBody))
  }
}
