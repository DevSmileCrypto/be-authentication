#!/bin/bash -ex
# shellcheck disable=SC2086

source .env

echo_validation_info() {
  echo "Cannot process script because env variable $1 is empty."
}

if [[ -z "$SPRING_PROFILE" ]]; then
    echo_validation_info "SPRING_PROFILE"
    exit 1
elif [[ -z "$LOCAL_IP" ]]; then
    echo_validation_info "LOCAL_IP"
    exit 1
elif [[ -z "$GRADLE_ENV" ]]; then
    echo_validation_info "GRADLE_ENV"
    exit 1
elif [[ -z "$PROJECT_PATH" ]]; then
    echo_validation_info "PROJECT_PATH"
    exit 1
elif [[ -z "$DOCKER_IMAGE_NAME" ]]; then
    echo_validation_info "DOCKER_IMAGE_NAME"
    exit 1
elif [[ -z "$DOCKER_CONTAINER_NAME" ]]; then
    echo_validation_info "DOCKER_CONTAINER_NAME"
    exit 1
elif [[ -z "$DOCKER_CONTAINER_INNER_PORT" ]]; then
    echo_validation_info "DOCKER_CONTAINER_INNER_PORT"
    exit 1
elif [[ -z "$DOCKER_CONTAINER_EXTERN_PORT" ]]; then
    echo_validation_info "DOCKER_CONTAINER_EXTERN_PORT"
    exit 1
fi

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

rebuild() {
  remove
  build
  start
  echo "$DOCKER_IMAGE_NAME image and $DOCKER_CONTAINER_NAME container rebuilt and restarted"
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
elif [[ "$1" == "rebuild" ]]; then
  rebuild
else
  build
  run
fi
