FROM openjdk:17
ARG JAR_FILE=test/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar","/app.jar"]