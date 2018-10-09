FROM openjdk:8-jdk-alpine
EXPOSE 8080:8080
VOLUME /tmp
COPY target/2fa-service-*.jar service.jar
ENTRYPOINT ["java", "-Xms256m", "-Xmx256m", "-XX:+UseG1GC", "-XX:+UseStringDeduplication", "-jar", "/service.jar"]