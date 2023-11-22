FROM openjdk:17
ENV APP_JWT_SECRET=daitssu
COPY build/libs/daitssu-api.jar /
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar","/daitssu-api.jar"]