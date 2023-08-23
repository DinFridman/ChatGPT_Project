FROM openjdk:17-alpine
COPY target/snapshot.jar app.jar
ARG OPEN_AI_KEY=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
ARG POSTGRES_PASSWORD=Din5675671
ENTRYPOINT ["java","-jar","/app.jar"]