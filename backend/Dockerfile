FROM openjdk:8-jre-alpine
ADD ./build/libs/backend-0.1.0.jar /app/teststore.jar
COPY ./scripts /scripts
WORKDIR /app
RUN apk add --update --no-cache netcat-openbsd bash
CMD /scripts/wait-for-cassandra.sh $CASSANDRA_HOST $CASSANDRA_PORT && java -jar /app/teststore.jar --cassandra.contactPoints=$CASSANDRA_HOST
