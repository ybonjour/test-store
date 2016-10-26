FROM java:8-jre
ADD ./backend/build/libs/teststore-0.1.0.jar /app/teststore.jar
ADD ./scripts /scripts
WORKDIR /app
RUN apt-get update && apt-get install -y netcat
CMD /scripts/wait-for-cassandra.sh $CASSANDRA_HOST $CASSANDRA_PORT && java -jar /app/teststore.jar --cassandra.contactPoints=$CASSANDRA_HOST
