#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
data_generator=$dir/..

docker build -t test-store-data-generator $data_generator
