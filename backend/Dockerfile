# ========================================================
# Stage 1: Build the application using Maven and JDK 21
# ========================================================
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and pre-fetch dependencies for efficient Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the production executable JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ========================================================
# Stage 2: Run the application using Eclipse Temurin JRE 21
# ========================================================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Set default fallback port; Render will inject the active $PORT dynamically
ENV PORT=8080
EXPOSE 8080

# Copy compiled JAR from build stage
COPY --from=build /app/target/travel-expense-calculator-*.jar app.jar

# Start application (binds to ${PORT} from environment as configured in application.properties)
ENTRYPOINT ["java", "-jar", "app.jar"]
