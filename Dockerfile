FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

# 🔥 FIX: give permission to mvnw
RUN chmod +x mvnw

# Build
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/jobportal-0.0.1-SNAPSHOT.jar"]