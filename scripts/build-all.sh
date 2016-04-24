#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

# Build backend
$dir/../gradlew assemble
docker build -t test-store-backend $dir/../backend

# Build web
docker run -v $dir/../web/www:/www --rm node bash -ce "(cd /www; npm install; npm run build)"
docker build -t test-store-web $dir/../web

# Build migration
docker build -t test-store-migration $dir/../migration