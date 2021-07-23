#1
FROM gradle:6.8.3-jdk11 AS BUILD_IMAGE

ARG project_path=.
ARG gradle_env=prod

RUN mkdir /apps
COPY --chown=gradle:gradle $project_path /apps
WORKDIR /apps

RUN gradle clean build

#2
FROM openjdk:11-jre
COPY --from=BUILD_IMAGE /apps/build/libs/be-authentication-0.0.1-SNAPSHOT.jar .
COPY --from=BUILD_IMAGE /apps/startup.sh .

RUN apt-get install curl; curl -sL https://deb.nodesource.com/setup_12.x | bash -; apt-get -y install nodejs
RUN cd script/; npm i; cd ..

CMD bash startup.sh