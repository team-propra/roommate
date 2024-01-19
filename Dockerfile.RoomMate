# Build stage
FROM gradle:jdk21 AS BUILD
WORKDIR /usr/app/
COPY . . 
RUN gradle build

# Build Runtime Image stage
FROM openjdk:21
ENV JAR_NAME="RoomMate-prod.jar"
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8080
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME  