# How to create a fresh database instance
Start a cassandra instance in a docker container named `cassandra`:
```
docker run -p 9042:9042 --name cassandra -d cassandra:2.2
```

Create the `teststore` keyspace on that Cassandra instance:
```
docker run -it --link cassandra:cassandra --rm -v $(pwd)/cassandra/:/scripts  cassandra:2.2 cqlsh cassandra -f /scripts/create_teststore_keypsace.cql
```

# How to run tests
To run the tests you need to have a Cassandra instance running (Yes, I know this is not ideal and it will be changed soon).
Execute the tests with

```
./gradlew test
```

# How to start the app
Simply run
```
./gradlew assemble
docker-compose up
```
then the service is avialable on your docker host on port 8080

```
curl http://$(docker-machine ip):8080/tests
```
