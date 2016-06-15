#!/bin/bash

curl -i -X POST -H "Content-Type:application/json" http://52.28.34.119:8080/add -d '{"title":"- First message -"}'
curl -i -X POST -H "Content-Type:application/json" http://52.28.34.119:8080/add -d '{"title":"- Second message -"}'
curl -i -X POST -H "Content-Type:application/json" http://52.28.34.119:8080/add -d '{"title":"- Third message -"}'
curl -i -X POST -H "Content-Type:application/json" http://52.28.34.119:8080/add -d '{"title":"- Fourth message -"}'
curl -i -X POST -H "Content-Type:application/json" http://52.28.34.119:8080/add -d '{"title":"- Fifth message -"}'
