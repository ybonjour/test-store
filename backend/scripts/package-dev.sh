#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
backend=$dir/..

docker build -t test-store-backend $backend
