#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
root=$dir/../..

docker run -v $root/:/workspace --rm java:8-jdk bash -ce "(cd workspace/backend; ../gradlew build)"
