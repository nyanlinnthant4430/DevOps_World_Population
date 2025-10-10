FROM openjdk:19


# Copy the fat JAR (includes all dependencies)
COPY ./target/DevOps_World_Population-0.1.0.2-jar-with-dependencies.jar /tmp
WORKDIR /tmp

# Run the application
ENTRYPOINT ["java", "-jar", "DevOps_World_Population-0.1.0.2-jar-with-dependencies.jar"]
