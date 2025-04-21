#!/bin/bash

ENV_TYPE=prod

sudo docker run --rm -d --name db --network host \
    --hostname=postgis.$ENV_TYPE-ix-aws-local \
	-e POSTGRES_USER=test_safety_management \
    -e POSTGRES_PASSWORD=test_safety_management \
    -e POSTGRES_DB=test_postgis \
    --health-cmd="pg_isready -U safety_management" \
    --health-interval=10s \
    --health-timeout=5s \
    --health-retries=5 \
    postgis/postgis:16-3.4

for i in {1..10}; do
    if docker inspect --format='{{.State.Health.Status}}' $(docker ps -q --filter "ancestor=postgis/postgis:16-3.4") | grep healthy; then
        echo "PostGIS is ready!";
        break
    fi;
    echo "Waiting for PostGIS to be healthy...";
    sleep 10;
done;

sql_file="safety-management/src/ddl/02_ddl_init_test.sql"
sudo docker cp $sql_file $(docker ps -q --filter "ancestor=postgis/postgis:16-3.4"):/data.sql
sudo docker exec $(docker ps -q --filter "ancestor=postgis/postgis:16-3.4") \
bash -c "psql -U test_safety_management -d test_postgis -f /data.sql"

sudo docker exec $(docker ps -q --filter "ancestor=postgis/postgis:16-3.4") \
bash -c "psql -U test_safety_management -d test_postgis -c 'SELECT * FROM test_safety_management.t_airway_reservation;'"

sudo docker run -d --rm --network host \
    --name activemq-classic5.18 \
    --expose 1883 apache/activemq-classic:5.18.4 

sudo docker build --build-arg TARGET_APP=safety-management --build-arg VERSION=0.0.1 --build-arg ENV_TYPE=$ENV_TYPE . --tag safety-management-$ENV_TYPE:latest  --network host
sudo docker stop db activemq-classic5.18