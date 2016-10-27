#!/bin/bash

backend_host=$1
backend_port=$2

echo Waiting for backend on ${backend_host}:${backend_port}
while ! nc -z ${backend_host} ${backend_port}; do
   sleep 1
done
echo "Backend is ready."
