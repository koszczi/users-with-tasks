#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/users_with_tasks/src
COPY pom.xml /home/users_with_tasks
RUN mvn --quiet -f /home/users_with_tasks/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/users_with_tasks/target/users-with-tasks-0.0.1-SNAPSHOT.jar /usr/local/lib/users-with-tasks.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "/usr/local/lib/users-with-tasks.jar"]