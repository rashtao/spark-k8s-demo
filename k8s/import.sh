#!/bin/bash

set -e
LOCATION=$(pwd)/$(dirname "$0")

for c in movies persons; do
  docker run --rm --net="host" -v "$LOCATION"/import:/import --entrypoint '' \
  docker.io/arangodb/arangodb:3.9.0 \
  arangoimp \
    --server.endpoint=http+tcp://localhost:8529 \
    --server.password=test \
    --type json \
    --file="/import/$c.jsonl" \
    --collection="$c" \
    --create-collection=true \
    --overwrite=true \
    --server.database=imdb \
    --create-database=true
done

for c in actedIn directed; do
  docker run --rm --net="host" -v "$LOCATION"/import:/import --entrypoint '' \
  docker.io/arangodb/arangodb:3.9.0 \
  arangoimp \
    --server.endpoint=http+tcp://localhost:8529 \
    --server.password=test \
    --type json \
    --file="/import/$c.jsonl" \
    --collection="$c" \
    --create-collection=true \
    --overwrite=true \
    --server.database=imdb \
    --create-database=true \
    --create-collection-type=edge
done

echo "DONE"
