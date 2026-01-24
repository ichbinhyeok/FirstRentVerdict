# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /home/gradle/project
COPY . .
RUN chmod +x gradlew
RUN ./gradlew :app:bootJar --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built jar
COPY --from=build /home/gradle/project/app/build/libs/*.jar app.jar

# JVM options requested by user
# -XX:+UseSerialGC: Optimized for lower memory footprints
# -Xms128m -Xmx256m: Memory constraints for lightweight OCI ARM instances
ENV JAVA_OPTS="-XX:+UseSerialGC -Xms128m -Xmx256m -Xss512k"

EXPOSE 8080

# Execute with JVM options
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
