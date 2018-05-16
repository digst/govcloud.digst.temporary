#!/usr/bin/env bash

docker exec schema-registry kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic $1 --property schema.registry.url=http://localhost:38081 --from-beginning