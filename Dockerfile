FROM openjdk:17-jdk

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

RUN cp target/*.jar app.jar

EXPOSE 8080
# redeploy trigger
ENTRYPOINT ["java", "-jar", "app.jar"]