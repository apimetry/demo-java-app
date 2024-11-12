.PHONY: build

build:
	gradle bootjar

run:
	java -javaagent:./opentelemetry-javaagent.jar -Dotel.service.name=my-app -Dotel.exporter.otlp.endpoint=http://localhost:4317 -jar ./build/libs/demo-java-app-0.0.1.jar

publish:
	docker build -t apimetry/demojavaapp:latest .
	docker push apimetry/demojavaapp:latest