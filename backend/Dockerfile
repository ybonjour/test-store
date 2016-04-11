FROM java:8-jre
ADD ./build/libs/teststore-0.1.0.jar /app/teststore.jar
WORKDIR /app
RUN apt-get update && apt-get install -y netcat
CMD java -jar /app/teststore.jar