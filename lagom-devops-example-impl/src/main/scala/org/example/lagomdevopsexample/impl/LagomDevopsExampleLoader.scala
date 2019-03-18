package org.example.lagomdevopsexample.impl

import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import org.example.lagomdevopsexample.api.LagomDevopsExampleService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import com.softwaremill.macwire._

class LagomDevopsExampleLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomDevopsExampleApplication(context) with LagomServiceLocatorComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomDevopsExampleApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomDevopsExampleService])
}

abstract class LagomDevopsExampleApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[LagomDevopsExampleService](wire[LagomDevopsExampleServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = LagomdevopsexampleSerializerRegistry

  // Register the lagom-devops-example persistent entity
  persistentEntityRegistry.register(wire[LagomDevopsExampleEntity])
}
