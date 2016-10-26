#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
root=$dir/..

# Build backend
$root/backend/scripts/build.sh
docker build -t test-store-backend $root/backend

# Build web
$root/web/scripts/build.sh
docker build -t test-store-web $root/web

# Build migration
docker build -t test-store-migration $root/migration
