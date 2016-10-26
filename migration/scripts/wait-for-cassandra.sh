#!/bin/bash

cassandra_host=$1
cassandra_port=$2

echo Waiting for cassndra on ${cassandra_host}:${cassandra_port}
while ! nc -z ${cassandra_host} ${cassandra_port}; do
   sleep 1
done
echo "Cassandra is ready."