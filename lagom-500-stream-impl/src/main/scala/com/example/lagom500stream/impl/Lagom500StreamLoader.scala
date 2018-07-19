package com.example.lagom500stream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.example.lagom500stream.api.Lagom500StreamService
import com.example.lagom500.api.Lagom500Service
import com.softwaremill.macwire._

class Lagom500StreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new Lagom500StreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new Lagom500StreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[Lagom500StreamService])
}

abstract class Lagom500StreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[Lagom500StreamService](wire[Lagom500StreamServiceImpl])

  // Bind the Lagom500Service client
  lazy val lagom500Service = serviceClient.implement[Lagom500Service]
}
