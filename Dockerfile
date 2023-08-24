FROM openjdk:17-alpine
COPY target/ChatGPT-Project-0.0.1-SNAPSHOT.jar app.jar
ARG OPEN_AI_KEY=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
ARG POSTGRES_PASSWORD=Din5675671
ENTRYPOINT ["java","-jar","/app.jar"]