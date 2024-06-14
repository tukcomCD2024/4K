FROM openjdk:17.0.2-jdk-slim-buster AS builder
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY . ./
COPY src/main/resources/application.properties /app/application.properties
RUN ./gradlew build -x test --stacktrace

FROM openjdk:17.0.2-jdk-slim-buster
ENV ARTIFACT_NAME=webChat-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY --from=builder $APP_HOME/build/libs/$ARTIFACT_NAME .
COPY --from=builder $APP_HOME/application.properties /app/application.properties
EXPOSE 8080
CMD ["java", "-jar", "webChat-0.0.1-SNAPSHOT.jar"]
