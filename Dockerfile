FROM openjdk:17
COPY build/libs/daitssu-api.jar /
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar","/daitssu-api.jar"]