#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

version=$(cat $dir/CURRENT_DISPLAY_VERSION)

docker build --tag ybonjour/test-store-web:$version .
docker build --tag ybonjour/test-store-web .

docker push ybonjour/test-store-web:$version
docker push ybonjour/test-store-web



