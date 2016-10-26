#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

# Build migratioin image
docker build -t test-store-migration-dev $dir/../migration

# Start Cassandra
docker run -p 9042:9042 --name cassandra -d cassandra

#Wait for cassandra
docker run --rm -v $dir/../scripts:/scripts --link cassandra:cassandra test-store-migration-dev /scripts/wait-for-cassandra.sh cassandra 9042

#Create keyspace
docker run -it --link cassandra:cassandra --rm -v $dir/../migration/:/cassandra  cassandra cqlsh cassandra -f /cassandra/create_keyspace.cql

#Migration
docker run --rm -it -v $dir/../migration/migrations:/migrations -v $dir/../migration:/workspace --link cassandra:cassandra test-store-migration-dev bash -ce "(cd workspace; ruby migrate.rb /migrations/ cassandra teststore 1)"
