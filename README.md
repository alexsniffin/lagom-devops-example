# lagom-devops-example

Sample of a lagom project with all of the boilerplate included for production.

### Set up
*refs. [Lightbend Orchestration](https://developer.lightbend.com/docs/lightbend-orchestration/current/)*
1. Set up the [reactive-cli](https://developer.lightbend.com/docs/lightbend-orchestration/current/setup/cli-installation.html)

### Requirements
* sbt 1.2.3^
* docker
* kubectl

### Building and Deployment

Building the docker image and publishing:
```bash
sbt docker:publishLocal
```
or for a remote docker registry:
```bash
sbt docker:publish
```

Verify:
```bash
docker images
```

Generating the k8's resources:
```bash
rp generate-kubernetes-resources "lagom-devops-example-impl:1.0-SNAPSHOT" \
   --generate-all \
   --ingress-annotation ingress.kubernetes.io/rewrite-target=/ \
   --ingress-annotation nginx.ingress.kubernetes.io/rewrite-target=/ \
   --pod-controller-replicas 2 \
   --env KAFKA_CLUSTER=kafka.namespace:9092 \
   --env CASSANDRA_CLUSTER=cassandra.namespace:9042 > lagom-devops-example-impl.yaml
```

Deploy the resources with kubectl:
```bash
kubectl -n namespace apply -f ./lagom-devops-example-impl.yaml
```

Verify:
```bash
kubectl get deployments
kubectl get pods
```

### Features Included in Bolierplate
* Service Locator
* Akka Cluster Bootstrap