#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

version=$(cat $dir/CURRENT_DISPLAY_VERSION)

docker build --tag ybonjour/test-store-backend:$version $dir
docker build --tag ybonjour/test-store-backend $dir

docker push ybonjour/test-store-backend:$version
docker push ybonjour/test-store-backend



