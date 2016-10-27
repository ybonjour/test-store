#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
web=$dir/..

docker build -t test-store-web $web
