#!/usr/bin/env bash

set -o errexit
set -o pipefail
set -o nounset

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

kubectl apply -f cassandra.yaml
kubectl apply -f migration.yaml
kubectl apply -f backend.yaml
kubectl apply -f frontend.yaml