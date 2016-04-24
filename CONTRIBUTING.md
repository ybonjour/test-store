# How to run tests
```
./gradlew test
```

# How to start the app
## With Docker Compose
Simply run
```
./scripts/build-all.sh
docker-compose up -d
```
then the service is avialable on your docker host on port `8080`

```
curl http://$(docker-machine ip):8080
```

## Without docker compose
### 1) Start cassandra
Start a cassandra instance in a docker container named `cassandra`:
```
docker run -p 9042:9042 --name cassandra -d cassandra
```

Create the `teststore` keyspace on that Cassandra instance:
```
docker run -it --link cassandra:cassandra --rm -v $(pwd)/cassandra/:/scripts  cassandra cqlsh cassandra -f /scripts/create_teststore_keypsace.cql
```

### 2) Start the Spring boot app
```
./gradlew bootRun
```
then the service is avialable on your `localhost` on port `8080`

```
curl http://localhost:8080
```
