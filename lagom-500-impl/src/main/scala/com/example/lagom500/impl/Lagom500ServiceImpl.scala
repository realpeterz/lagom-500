package com.example.lagom500.impl

import com.example.lagom500.api
import com.example.lagom500.api.Lagom500Service
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}
import play.api.Logger

import scala.concurrent.ExecutionContext

/**
  * Implementation of the Lagom500Service.
  */
class Lagom500ServiceImpl(persistentEntityRegistry: PersistentEntityRegistry)(implicit executionContext: ExecutionContext) extends Lagom500Service {

  override def hello(id: String, from: String) = ServiceCall { _ =>

    Logger(this.getClass).logger.info("hello ServiceCall invoked")
    // Look up the lagom-500 entity for the given ID.
    val ref = persistentEntityRegistry.refFor[Lagom500Entity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id)) map (_.concat(s" from $from"))
  }

  override def useGreeting(id: String) = ServiceCall { request =>

    // Look up the lagom-500 entity for the given ID.
    val ref = persistentEntityRegistry.refFor[Lagom500Entity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(Lagom500Event.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[Lagom500Event]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
