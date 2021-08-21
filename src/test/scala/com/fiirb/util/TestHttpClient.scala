package com.fiirb.util

import cats.MonadThrow
import cats.effect.{IO, Resource}
import fs2.Stream
import munit.CatsEffectSuite
import org.http4s.client.Client
import org.http4s.{Response, Status, Uri}

trait TestHttpClient {
  self: CatsEffectSuite =>

  protected def httpClient(respBody: String = "",
                           uri: Uri,
                           status: Status = Status.Ok)(implicit F: MonadThrow[IO]): Client[IO] = Client.apply[IO] { req =>

    assertEquals(req.uri, uri)

    Resource.eval(IO(Response[IO](body = Stream.emits(respBody.getBytes("UTF-8")), status = status)))
  }

}
