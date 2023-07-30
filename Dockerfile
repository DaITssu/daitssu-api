FROM openjdk:17
COPY common/build/libs/common.jar /
ENTRYPOINT ["java", "-Dspring.profiles.active=dev -Dspring.datasource.url=jdbc:postgresql://daitssu-rds-postgres-db.ccf8zpssvvpc.ap-northeast-2.rds.amazonaws.com/dev_daitssu -Dspring.datasource.username=dev_not_dev_because_some_yuchul -Dspring.datasource.password=THIS!()is90N$O$T$pass!Q@Wword1q2w", "-jar","/common.jar"]