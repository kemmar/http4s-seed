package com.fiirb.util

import com.fiirb.domain.user.UserInformation
import org.http4s.Uri

trait ServiceBase[F[_], T] {
  def endpointUri: String

  lazy val endpoint: Uri = Uri.fromString(endpointUri).fold(
    throw _,
    res => res
  )

  def getCreditCards(userInformation: UserInformation): F[List[T]]
}
