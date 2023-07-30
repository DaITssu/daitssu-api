FROM openjdk:17
COPY common/build/libs/common.jar /
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar","/common.jar"]