# How to create a fresh database instance
Spring boot does not yet work with Cassandra 3. So you have to use Cassandra 2.2


```
docker run -p 9042:9042 cassandra:2.2
```

Find out the name of the cassandra docker container using `docker ps` and then execute:

```
docker run -it --link <cassandra_docker_container_name>:cassandra --rm -v $(pwd)/cassndra/:/scripts  cassandra:2.2 cqlsh cassandra -f /scripts/create_teststore_keypsace.cql
```