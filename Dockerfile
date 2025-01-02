FROM gradle:8.10.0-jdk21 AS build
WORKDIR /app
COPY src ./src
COPY build.gradle ./build.gradle
RUN gradle bootjar
RUN mkdir /app/data

FROM openjdk:21-bullseye
WORKDIR /app

COPY --from=build /app/build/libs/app-0.0.1.jar ./app.jar
COPY opentelemetry-javaagent.jar /optel-agent.jar

ENTRYPOINT ["java", "-javaagent:/optel-agent.jar", "-Dotel.service.name=my-app", "-Dotel.exporter.otlp.endpoint=http://collector:4317","-jar", "/app/app.jar"]
CMD []