#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
root=$dir/../..

uid=$(id -u)
gid=$(id -g)

docker run -v $root/:/workspace -u $uid:$gid -e GRADLE_USER_HOME=/workspace/work-directory --rm openjdk:8-jdk bash -ce "(cd workspace/backend; ../gradlew build)"
