#!/bin/bash

curl -i -X POST -H "Content-Type:application/json" http://52.28.34.119:8080/add -d '{"title":"- Invalid message -}'
