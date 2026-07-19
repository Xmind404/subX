#!/usr/bin/env bash
set -e
./gradlew installDist --console=plain -q
exec ./app/build/install/app/bin/app