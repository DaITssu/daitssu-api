FROM openjdk:17
COPY common/build/libs/common.jar /
ENTRYPOINT ["java", "-Dspring.config.location=file:./config/*/,./config/ -Dspring.profiles.active=dev -Dspring.datasource.url=jdbc:postgresql://daitssu-rds-postgres-db.ccf8zpssvvpc.ap-northeast-2.rds.amazonaws.com:5432/daitssu -Dspring.datasource.username=daitssuadmin -Dspring.datasource.password=Notadmin!", "-jar","/common.jar"]