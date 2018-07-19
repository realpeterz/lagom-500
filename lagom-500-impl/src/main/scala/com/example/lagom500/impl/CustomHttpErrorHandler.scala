package com.example.lagom500.impl

import com.lightbend.lagom.scaladsl.api.transport.TransportErrorCode
import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.{RequestHeader, Result, Results}

import scala.concurrent.Future

class CustomHttpErrorHandler extends HttpErrorHandler {

  override def onClientError(request: RequestHeader,
                             statusCode: Int,
                             message: String): Future[Result] =
    Future.successful(
      new Results.Status(statusCode).apply(
        Json.obj(
          "code" -> statusCode,
          "error" -> message
        )
      )
    )

  override def onServerError(request: RequestHeader,
                             exception: Throwable): Future[Result] = {
    val errorCode = exception match {
      case _: IllegalArgumentException => TransportErrorCode.BadRequest
      case _                           => TransportErrorCode.InternalServerError
    }

    Future.successful(
      new Results.Status(errorCode.http).apply(
        Json.obj(
          "code" -> 500,
          "error" -> exception.getMessage
        )
      )
    )
  }
}
