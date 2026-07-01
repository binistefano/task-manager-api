FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

# Create a dedicated user and group
RUN groupadd --system app && \
    useradd --system --gid app app

COPY --from=builder \
    /app/target/taskmanager-0.0.1-SNAPSHOT.jar \
    app.jar

# Give ownership of the application files
RUN chown -R app:app /app

# Switch to the non-root user
USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]