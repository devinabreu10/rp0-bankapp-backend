FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/p0-bankapp-remake-0.0.1-SNAPSHOT.jar p0-bankapp-remake.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","p0-bankapp-remake.jar"]