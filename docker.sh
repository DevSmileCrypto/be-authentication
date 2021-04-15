#!/bin/bash -ex

source .env

docker build -t $DOCKER_IMAGE_NAME \
  --build-arg gradle_env=$GRADLE_ENV \
  --build-arg project_path=$PROJECT_PATH \
  --no-cache \
  .

docker run --name $DOCKER_CONTAINER_NAME \
 -p $DOCKER_CONTAINER_EXTERN_PORT:$DOCKER_CONTAINER_INNER_PORT \
 -e ENVIRONMENT=$SPRING_PROFILE \
 -e LOCAL_IP=$LOCAL_IP \
 -d $DOCKER_IMAGE_NAME