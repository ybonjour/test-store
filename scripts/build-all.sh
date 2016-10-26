#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

# Build backend
docker run -v $dir/../:/workspace --rm java:8-jdk bash -ce "(cd workspace; ./gradlew assemble)"
docker build -t test-store-backend $dir/../backend

# Build web
docker run -v $dir/../web/www:/www --rm node bash -ce "(cd /www; npm install; npm run tsc)"
docker build -t test-store-web $dir/../web

# Build migration
docker build -t test-store-migration $dir/../migration
