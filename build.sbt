organization in ThisBuild := "org.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

/**
  * @see https://developer.lightbend.com/docs/lightbend-orchestration/current/features/akka-cluster-bootstrap.html &
  *      https://developer.lightbend.com/docs/lightbend-orchestration/current/setup/project-configuration.html#additional-configuration
  */
rpEnableAkkaClusterBootstrap := true
rpAkkaClusterBootstrapSystemName := "my-actor-system"

rpEndpoints += HttpEndpoint("http", HttpIngress(Vector(80, 443), Vector.empty, Vector("/api/hello")))

// Connect to remote Kafka through kubernetes DNS
lagomKafkaEnabled in ThisBuild := false
lagomKafkaAddress in ThisBuild := sys.env.getOrElse("KAFKA_CLUSTER", "kafka.namespace:9092")

// Connect to remote Cassandra through kubernetes DNS
lagomCassandraEnabled in ThisBuild := false
lagomUnmanagedServices in ThisBuild := Map("cas_native" -> sys.env.getOrElse("CASSANDRA_CLUSTER", "cassandra.namespace:9042"))

val rpCommonVersion = "1.7.0"
val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `lagom-devops-example` = (project in file("."))
    .aggregate(`lagom-devops-example-api`, `lagom-devops-example-impl`, `lagom-devops-example-stream-api`, `lagom-devops-example-stream-impl`)

lazy val `lagom-devops-example-api` = (project in file("lagom-devops-example-api"))
    .settings(
      libraryDependencies ++= Seq(
        lagomScaladslApi
      )
    )

lazy val `lagom-devops-example-impl` = (project in file("lagom-devops-example-impl"))
    .enablePlugins(LagomScala)
    .enablePlugins(SbtReactiveAppPlugin)
    .settings(
      libraryDependencies ++= Seq(
        lagomScaladslPersistenceCassandra,
        lagomScaladslKafkaBroker,
        lagomScaladslTestKit,
        "com.lightbend.rp" %% "reactive-lib-common" % rpCommonVersion,
        "com.lightbend.rp" %% "reactive-lib-service-discovery-lagom14-scala" % rpCommonVersion,
        macwire,
        scalaTest
      )
    )
    .settings(lagomForkedTestSettings: _*)
    .dependsOn(`lagom-devops-example-api`)

lazy val `lagom-devops-example-stream-api` = (project in file("lagom-devops-example-stream-api"))
    .settings(
      libraryDependencies ++= Seq(
        lagomScaladslApi
      )
    )

lazy val `lagom-devops-example-stream-impl` = (project in file("lagom-devops-example-stream-impl"))
    .enablePlugins(LagomScala)
    .enablePlugins(SbtReactiveAppPlugin)
    .settings(
      libraryDependencies ++= Seq(
        lagomScaladslTestKit,
        "com.lightbend.rp" %% "reactive-lib-common" % rpCommonVersion,
        "com.lightbend.rp" %% "reactive-lib-service-discovery-lagom14-scala" % rpCommonVersion,
        macwire,
        scalaTest
      )
    )
    .dependsOn(`lagom-devops-example-stream-api`, `lagom-devops-example-api`)
