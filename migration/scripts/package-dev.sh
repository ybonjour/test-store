#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
migration=$dir/..

docker build -t test-store-migration $migration
