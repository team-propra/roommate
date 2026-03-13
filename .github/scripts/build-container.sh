#!/usr/bin/env bash
set -euo pipefail

# Args:
# 1 = component (e.g. roommate, keymaster)
# 2 = branch tag (e.g. v1.2.3)

COMPONENT="$1"
BRANCH_TAG="$2"

IMAGE_BASE="registry.massivecreationlab.com/roommate/${COMPONENT}"

echo "$REGISTRY_PASSWORD" | docker login "registry.massivecreationlab.com" -u "$REGISTRY_USERNAME" --password-stdin

docker build . \
  -t "${IMAGE_BASE}:${BRANCH_TAG}" \
  -t "${IMAGE_BASE}:latest" \
  -f "./Dockerfile.${COMPONENT}"

docker push "${IMAGE_BASE}:${BRANCH_TAG}"
docker push "${IMAGE_BASE}:latest"