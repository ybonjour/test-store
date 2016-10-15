# How to run tests
```
./gradlew test
```

# How to start the app
## With Docker Compose
Simply run
```
./scripts/build-all.sh
docker-compose up -d
```
then the service is avialable on your docker host on port `8080`

```
curl http://$(docker-machine ip):8080
```
