#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
root=$dir/..

image=ybonjour/test-store-web
version=$(cat $root/CURRENT_DISPLAY_VERSION)

docker build --tag $image:$version $root
docker build --tag $image $root

docker push $image:$version
docker push $image
