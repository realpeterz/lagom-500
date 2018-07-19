package com.example.lagom500.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.example.lagom500.api.Lagom500Service
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._
import play.api.http.HttpErrorHandler

class Lagom500Loader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new Lagom500Application(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new Lagom500Application(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[Lagom500Service])
}

abstract class Lagom500Application(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[Lagom500Service](wire[Lagom500ServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: Lagom500SerializerRegistry.type = Lagom500SerializerRegistry

  // Register the lagom-500 persistent entity
  persistentEntityRegistry.register(wire[Lagom500Entity])

  override lazy val httpErrorHandler: HttpErrorHandler = wire[CustomHttpErrorHandler]
}
