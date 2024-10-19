FROM gradle:8.10.0-jdk21
WORKDIR /app
COPY src ./src
COPY build.gradle ./build.gradle
RUN gradle bootjar

COPY opentelemetry-javaagent.jar /optel-agent.jar

CMD ["java", "-javaagent:/optel-agent.jar", "-Dotel.service.name=my-app", "-Dotel.exporter.otlp.endpoint=http://collector:4317","-jar", "/app/build/libs/app-0.0.1.jar"]
