#!/usr/bin/env sh
# Simplified Gradle wrapper launcher without committing binary artifacts.
# It downloads the Gradle distribution declared in gradle-wrapper.properties
# and invokes it directly. This keeps the repository free from binary files
# while providing the same UX as the default wrapper script.

set -e

SCRIPT_DIR="$(CDPATH= cd -- "$(dirname "$0")" && pwd)"
WRAPPER_DIR="$SCRIPT_DIR/gradle/wrapper"
PROPERTIES_FILE="$WRAPPER_DIR/gradle-wrapper.properties"

if [ ! -f "$PROPERTIES_FILE" ]; then
  echo "gradle-wrapper.properties not found" >&2
  exit 1
fi

# Extract configuration from the properties file
DIST_URL=$(grep '^distributionUrl=' "$PROPERTIES_FILE" | cut -d'=' -f2- | sed 's#\\:#:#g')
DIST_SHA=$(grep '^distributionSha256Sum=' "$PROPERTIES_FILE" | cut -d'=' -f2-)

if [ -z "$DIST_URL" ]; then
  echo "distributionUrl is not defined in gradle-wrapper.properties" >&2
  exit 1
fi

DIST_NAME="$(basename "$DIST_URL")"
DIST_BASE="${DIST_NAME%.zip}"
VERSION="$(printf "%s" "$DIST_NAME" | sed -E 's/gradle-([0-9.]+).*/\1/')"

GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
DOWNLOAD_DIR="$GRADLE_USER_HOME/wrapper/dists"
DIST_DIR="$DOWNLOAD_DIR/$DIST_BASE"
INSTALL_DIR="$DIST_DIR/gradle-$VERSION"
ZIP_PATH="$DOWNLOAD_DIR/$DIST_NAME"

mkdir -p "$DOWNLOAD_DIR"

if [ ! -d "$INSTALL_DIR" ]; then
  if [ ! -f "$ZIP_PATH" ]; then
    echo "Downloading Gradle distribution $DIST_NAME..."
    if command -v curl >/dev/null 2>&1; then
      curl --fail --location --output "$ZIP_PATH" "$DIST_URL"
    elif command -v wget >/dev/null 2>&1; then
      wget -O "$ZIP_PATH" "$DIST_URL"
    else
      echo "Neither curl nor wget is installed" >&2
      exit 1
    fi
  fi

  if [ -n "$DIST_SHA" ]; then
    echo "$DIST_SHA  $ZIP_PATH" | sha256sum --check --status || {
      echo "Checksum verification failed" >&2
      rm -f "$ZIP_PATH"
      exit 1
    }
  fi

  echo "Extracting Gradle..."
  rm -rf "$DIST_DIR"
  mkdir -p "$DIST_DIR"
  unzip -q "$ZIP_PATH" -d "$DIST_DIR"
fi

GRADLE_BIN="$INSTALL_DIR/bin/gradle"
if [ ! -x "$GRADLE_BIN" ]; then
  echo "Gradle executable not found at $GRADLE_BIN" >&2
  exit 1
fi

exec "$GRADLE_BIN" "${@}" 
