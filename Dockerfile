FROM openjdk:17
COPY target/ChatGPT-Project-snapshot.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]