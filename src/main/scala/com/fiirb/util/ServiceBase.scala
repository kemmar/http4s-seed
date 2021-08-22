package com.fiirb.util

import org.http4s.Uri

trait ServiceBase[F[_], T] {
  def endpointUri: String

  lazy val endpoint: Uri = Uri.fromString(endpointUri).fold(
    throw _,
    res => res
  )
}
