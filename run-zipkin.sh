#!/bin/bash
docker run -d -p 9411:9411 --name zipkin-slim openzipkin/zipkin-slim

