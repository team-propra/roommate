#!/usr/bin/env bash
set -euo pipefail

COMPONENT="$1"

# Args:
# 1 = component (e.g. roommate, keymaster)

IMAGE_BASE="registry.massivecreationlab.com/roommate/${COMPONENT}"

docker build . \
  -t "${IMAGE_BASE}:test" \
  -f "./Dockerfile.${COMPONENT}"