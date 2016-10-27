#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
root=$dir/..

# Build backend
$root/backend/scripts/build.sh
$root/backend/scripts/package-dev.sh

# Build web
$root/web/scripts/build.sh
$root/web/scripts/package-dev.sh

# Build migration
$root/migration/scripts/package-dev.sh
