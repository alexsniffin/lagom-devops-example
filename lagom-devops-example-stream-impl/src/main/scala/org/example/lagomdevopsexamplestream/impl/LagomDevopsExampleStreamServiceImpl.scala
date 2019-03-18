package org.example.lagomdevopsexamplestream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.example.lagomdevopsexamplestream.api.LagomDevopsExampleStreamService
import org.example.lagomdevopsexample.api.LagomDevopsExampleService

import scala.concurrent.Future

/**
  * Implementation of the LagomdevopsexampleStreamService.
  */
class LagomDevopsExampleStreamServiceImpl(lagomdevopsexampleService: LagomDevopsExampleService) extends LagomDevopsExampleStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagomdevopsexampleService.hello(_).invoke()))
  }
}
