#!/bin/bash -ex
java -jar -Dspring.profiles.active="${ENVIRONMENT}" -Duser.timezone=UTC -Duser.language=en -Duser.region=US be-authentication-0.0.1-SNAPSHOT.jar