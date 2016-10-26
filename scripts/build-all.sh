#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
root=$dir/..

# Build backend
docker run -v $root/:/workspace --rm java:8-jdk bash -ce "(cd workspace; ./gradlew assemble)"
docker build -t test-store-backend $root/backend

# Build web
docker run -v $root/web/www:/www --rm node bash -ce "(cd /www; npm install; npm run tsc)"
docker build -t test-store-web $root/web

# Build migration
docker build -t test-store-migration $root/migration
