# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-22-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to download dependencies
COPY pom.xml .

# Download dependencies without the source files
RUN mvn dependency:go-offline

# Copy the source code into the container
COPY src ./src
COPY wait-for-it.sh ./wait-for-it.sh

# Package the application into a JAR file
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:22

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["./wait-for-it.sh", "kafka:9092", "--", "java", "-jar", "app.jar"]
