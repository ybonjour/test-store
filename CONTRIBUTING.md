# How to create a fresh database instance
Spring boot does not yet work with Cassandra 3. So you have to use Cassandra 2.2


```
docker run -p 9042:9042 -d cassandra:2.2
```

Find out the name of the cassandra docker container using `docker ps` and then execute:

```
docker run -it --link <cassandra_docker_container_name>:cassandra --rm -v $(pwd)/cassandra/:/scripts  cassandra:2.2 cqlsh cassandra -f /scripts/create_teststore_keypsace.cql
```

# How to run tests
To run the tests you need to have a Cassandra instance running (Yes, I know this is not ideal and it will be changed soon).
Execute the tests with

```
./gradlew test
````

# How to start the app
Simply run
```
docker-compose up
```
then the service is avialable on your docker host on port 8080

```
curl http://$(docker-machine ip):8080/tests
```