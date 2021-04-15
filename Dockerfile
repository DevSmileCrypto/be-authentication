#1
FROM gradle:6.4.1-jdk11 AS BUILD_IMAGE

ARG project_path=.
ARG gradle_env=prod

RUN mkdir /apps
COPY --chown=gradle:gradle $project_path /apps
WORKDIR /apps

RUN gradle -Penv=$gradle_env clean build

#2
FROM openjdk:11-jre
COPY --from=BUILD_IMAGE /apps/build/libs/be-authentication-0.0.1-SNAPSHOT.jar .
COPY --from=BUILD_IMAGE /apps/startup.sh .

CMD bash startup.sh