#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

version=$(cat $dir/CURRENT_DISPLAY_VERSION)

docker build --tag ybonjour/test-store-migration:$version $dir
docker build --tag ybonjour/test-store-migration $dir

docker push ybonjour/test-store-migration:$version
docker push ybonjour/test-store-migration



