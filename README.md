# ArangoDB Spark Datasource Demo

This demo is composed of 2 parts:

- `ReadDemo`: reads the ArangoDB collections created above as Spark Dataframes, specifying columns selection and records
  filters predicates or custom AQL queries
- `ReadWriteDemo`: reads the ArangoDB collections created above as Spark Dataframes, applies projections and filtering,
  writes to a new ArangoDB collection

## Requirements

This demo requires:

- JDK 11
- maven
- docker
- minikube

## Prepare the environment

```shell
# install Spark 3.1.2
minikube start --cpus 4 --memory 8192
eval $(minikube -p minikube docker-env)
cd $SPARK_HOME
./bin/docker-image-tool.sh \
  -m \
  -b java_image_tag=11-jre-slim \
  -t v3.1.2 \
  build

kubectl create ns spark-demo
kubectl apply -f k8s/adb.yaml
kubectl wait --for=condition=ready pod -l app=adb -n spark-demo
kubectl port-forward service/adb 8529:8529 -n spark-demo &

# ArangoDB must be reachable using the same hostname in the driver and in the executors
sudo echo "127.0.0.1 adb" >> /etc/hosts

./k8s/import.sh

K8S_SERVER=$(kubectl config view --output=jsonpath='{.clusters[].cluster.server}')
mvn clean test -DsparkMaster=k8s://$K8S_SERVER
```

## ref

- https://minikube.sigs.k8s.io/docs/start/
- https://jaceklaskowski.github.io/spark-kubernetes-book/demo/spark-shell-on-minikube/
- https://minikube.sigs.k8s.io/docs/tutorials/setup_minikube_in_github_actions/
