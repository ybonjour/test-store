FROM cassandra
ADD ./migration /migration
ADD ./scripts /scripts
ADD ./cassandra /cassandra
RUN apt-get update && apt-get install -y netcat && apt-get install -y ruby
ENTRYPOINT /scripts/wait-for-cassandra.sh $CASSANDRA_HOST $CASSANDRA_PORT && cqlsh cassandra -f /cassandra/create_keyspace.cql && (cd /migration; ruby migrate.rb /cassandra/migrations/ $CASSANDRA_HOST teststore 1)
