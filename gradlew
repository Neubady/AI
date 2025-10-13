#!/usr/bin/env sh
set -e
DIR="$(dirname "$0")"
exec "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
