#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
root=$dir/..

docker_compose="$root/docker-compose-dev.yml"

$dir/build-package-dev.sh

docker-compose -f $docker_compose down

docker-compose -f $docker_compose up