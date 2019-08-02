#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
root=$dir/../..

docker run -v $root/web/www:/www --rm node bash -ce "(cd /www; npm install; npm run typings install; npm run tsc)"
docker run -v $root/web/www2:/www2 --rm node bash -ce "(cd /www2; npm clean-install; npm run build)"

