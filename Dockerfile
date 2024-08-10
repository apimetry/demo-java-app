FROM openjdk:23-ea-17-slim

COPY opentelemetry-javaagent.jar /optel-agent.jar
COPY build/libs/demo-java-app-0.0.1.jar /app.jar

CMD ["java", "-javaagent:/optel-agent.jar", "-Dotel.service.name=my-app", "-Dotel.exporter.otlp.endpoint=http://collector:4317","-jar", "/app.jar"]