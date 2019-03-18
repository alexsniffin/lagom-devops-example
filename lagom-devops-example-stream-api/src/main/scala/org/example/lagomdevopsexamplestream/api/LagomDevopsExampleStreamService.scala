package org.example.lagomdevopsexamplestream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

/**
  * The lagom-devops-example stream interface.
  *
  * This describes everything that Lagom needs to know about how to serve and
  * consume the LagomdevopsexampleStream service.
  */
trait LagomDevopsExampleStreamService extends Service {

  def stream: ServiceCall[Source[String, NotUsed], Source[String, NotUsed]]

  override final def descriptor: Descriptor = {
    import Service._

    named("lagom-devops-example-stream")
      .withCalls(
        namedCall("stream", stream)
      ).withAutoAcl(true)
  }
}

