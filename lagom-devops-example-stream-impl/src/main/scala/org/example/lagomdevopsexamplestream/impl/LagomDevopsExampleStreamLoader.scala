package org.example.lagomdevopsexamplestream.impl

import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import com.softwaremill.macwire._
import org.example.lagomdevopsexample.api.LagomDevopsExampleService
import org.example.lagomdevopsexamplestream.api.LagomDevopsExampleStreamService
import play.api.libs.ws.ahc.AhcWSComponents

class LagomDevopsExampleStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomDevopsExampleStreamApplication(context) with LagomServiceLocatorComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomDevopsExampleStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomDevopsExampleStreamService])
}

abstract class LagomDevopsExampleStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[LagomDevopsExampleStreamService](wire[LagomDevopsExampleStreamServiceImpl])

  // Bind the LagomdevopsexampleService client
  lazy val lagomdevopsexampleService: LagomDevopsExampleService = serviceClient.implement[LagomDevopsExampleService]
}
