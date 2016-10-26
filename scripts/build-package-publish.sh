#!/bin/bash

set -e

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
root=$dir/..


$root/backend/scripts/build.sh
$root/backend/scripts/package-and-publish.sh

$root/web/scripts/build.sh
$root/web/scripts/package-and-publish.sh

$root/migration/scripts/package-and-publish.sh

