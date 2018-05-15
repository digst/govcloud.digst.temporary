#!/usr/bin/env bash
docker exec kafka kafka-topics --describe --topic $1 --zookeeper localhost:2181
