# Build stage: Run `mvn clean package` for latest jar 
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
# openjdk images are deprecated, using amazoncorretto as alternative
FROM amazoncorretto:17

ARG APP_VERSION=1.0.0
ARG PROFILE=dev
ARG GC_LOG_OPTS=-Xlog:gc*:file\=gc.log:time,uptime,level,tags

WORKDIR /app
COPY --from=build /build/target/rp0-bankapp-backend-*.jar /app/

EXPOSE 8080

ENV JAR_VERSION=${APP_VERSION}
ENV ACTIVE_PROFILE=${PROFILE}
ENV GC_LOG_OPTS=${GC_LOG_OPTS}

# ENTRYPOINT ["java","-jar","rp0-bankapp-backend-${JAR_VERSION}.jar"]
CMD java -jar ${GC_LOG_OPTS} -Dspring.profiles.active=${ACTIVE_PROFILE} rp0-bankapp-backend-${JAR_VERSION}.jar