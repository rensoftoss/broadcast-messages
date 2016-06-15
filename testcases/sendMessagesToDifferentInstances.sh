#!/bin/bash

curl -i -X POST -H "Content-Type:application/json" http://52.28.34.119:8080/add -d '{"title":"- One message on first instance -"}'
sleep 5
curl -i -X POST -H "Content-Type:application/json" http://52.28.34.119:8180/add -d '{"title":"- One message on second instance -"}'
