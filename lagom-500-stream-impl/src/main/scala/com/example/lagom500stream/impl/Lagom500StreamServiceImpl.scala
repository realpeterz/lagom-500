package com.example.lagom500stream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.example.lagom500stream.api.Lagom500StreamService
import com.example.lagom500.api.Lagom500Service

import scala.concurrent.Future

/**
  * Implementation of the Lagom500StreamService.
  */
class Lagom500StreamServiceImpl(lagom500Service: Lagom500Service) extends Lagom500StreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagom500Service.hello(_, "streaming source").invoke()))
  }
}
