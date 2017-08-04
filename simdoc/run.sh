#!/bin/sh

echo "Launch clustering-server in background"
nohup java -jar clustering-server/*.jar --server.port=1958 &

echo "Launch mediation-server"
java -jar mediation-server/*.jar --server.port=2407 --clustering.server.url=http://localhost:1958
