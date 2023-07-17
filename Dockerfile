FROM openjdk:17
COPY test/build/libs/test.jar /
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar","/test.jar"]