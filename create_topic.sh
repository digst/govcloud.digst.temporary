#!/usr/bin/env bash
docker exec kafka kafka-topics --create --topic $1 --partitions 1 --replication-factor 1 --if-not-exists --config retention.ms=100000 --zookeeper localhost:2181
