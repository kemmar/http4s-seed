package com.fiirb.error

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.Uri.Path
import org.http4s.circe.jsonEncoderOf

object Errors {

  trait SystemError extends RuntimeException {
    val error: String
  }

  case class RequestError(error: String) extends SystemError

  case class UriError(uri: String, error: String) extends SystemError

  case class ServiceError(serviceName: String, error: String) extends SystemError

  case class ApplicationError(endpoint: Path, errorName: String, errorMsg: String)

  object ApplicationError {
    def apply(path: Path, serviceError: ServiceError): ApplicationError = {
      ApplicationError(path, s"Failed service call: ${serviceError.serviceName}", serviceError.error)
    }

    def apply(path: Path, requestError: RequestError): ApplicationError = {
      ApplicationError(path, s"Invalid json", requestError.error)
    }

    def apply(path: Path, uriError: UriError): ApplicationError = {
      ApplicationError(path, "Invalid service URI ", uriError.error)
    }

    implicit val applicationErrorEncoder: Encoder[ApplicationError] = deriveEncoder[ApplicationError]

    implicit def applicationErrorEntityEncoder[F[_]]: EntityEncoder[F, ApplicationError] =
      jsonEncoderOf
  }

}
