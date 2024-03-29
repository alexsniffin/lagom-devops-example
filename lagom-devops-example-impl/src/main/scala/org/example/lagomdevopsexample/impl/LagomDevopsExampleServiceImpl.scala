package org.example.lagomdevopsexample.impl

import org.example.lagomdevopsexample.api
import org.example.lagomdevopsexample.api.LagomDevopsExampleService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the LagomdevopsexampleService.
  */
class LagomDevopsExampleServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends LagomDevopsExampleService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the lagom-devops-example entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagomDevopsExampleEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the lagom-devops-example entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagomDevopsExampleEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(LagomdevopsexampleEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[LagomdevopsexampleEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
