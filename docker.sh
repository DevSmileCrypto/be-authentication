#!/bin/bash -ex
# shellcheck disable=SC2086

source .env

build() {
  docker build -t $DOCKER_IMAGE_NAME \
  --build-arg gradle_env=$GRADLE_ENV \
  --build-arg project_path=$PROJECT_PATH \
  --no-cache \
  .
  echo "Image built"
}

run() {
  id=$(docker ps -a | grep $DOCKER_CONTAINER_NAME | awk '{print $1}')
  if [[ ! -z "$id" ]]; then
      echo "Container with $DOCKER_CONTAINER_NAME already launched. Please stop and remove it for run again."
      return 1
  fi

  docker run --name $DOCKER_CONTAINER_NAME \
  -p $DOCKER_CONTAINER_EXTERN_PORT:$DOCKER_CONTAINER_INNER_PORT \
  -e ENVIRONMENT=$SPRING_PROFILE \
  -e LOCAL_IP=$LOCAL_IP \
  -d $DOCKER_IMAGE_NAME

  echo "$DOCKER_CONTAINER_NAME container launched"
}

start() {
  docker start $DOCKER_CONTAINER_NAME
  echo "$DOCKER_CONTAINER_NAME container started"
}

stop() {
  docker stop $DOCKER_CONTAINER_NAME
  echo "$DOCKER_CONTAINER_NAME container stopped"
}

restart() {
  stop
  start
  echo "$DOCKER_CONTAINER_NAME container restarted"
}

remove() {
  docker rm -f $DOCKER_CONTAINER_NAME
  docker rmi -f $DOCKER_IMAGE_NAME
  echo "$DOCKER_CONTAINER_NAME container and $DOCKER_IMAGE_NAME image removed"
}

if [[ "$1" == "build" ]]; then
  build
elif [[ "$1" == "run" ]]; then
  run
elif [[ "$1" == "start" ]]; then
  start
elif [[ "$1" == "stop" ]]; then
  stop
elif [[ "$1" == "restart" ]]; then
  restart
elif [[ "$1" == "remove" ]]; then
  remove
else
  build
  run
fi
