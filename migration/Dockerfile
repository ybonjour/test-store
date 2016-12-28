FROM cassandra
ADD . /migration
ADD ./scripts /scripts
RUN apt-get update && apt-get install -y netcat && apt-get install -y ruby && apt-get install -y build-essential && apt-get install -y ruby-dev
RUN bash -c "gem install bundler"
RUN bash -c "(cd /migration; bundle install)"
ENTRYPOINT /scripts/wait-for-cassandra.sh $CASSANDRA_HOST $CASSANDRA_PORT && cqlsh cassandra -f /migration/create_keyspace.cql && (cd /migration; ruby migrate.rb /migration/migrations/ $CASSANDRA_HOST teststore 1)
