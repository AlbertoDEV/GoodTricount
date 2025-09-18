# Stage 1: Build the application with Maven
FROM maven:3.9.6-eclipse-temurin-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean install

# Stage 2: Create the final image with Tomcat
FROM tomcat:9.0

# Invalidate the cache
ENV CACHE_BUSTER=1

# Remove the default Tomcat welcome page
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy the .war file from the build stage to Tomcat's webapps directory
COPY --from=build /app/target/goodtricount.war /usr/local/tomcat/webapps/ROOT.war

# Set CATALINA_OPTS to disable cgroup v2 metrics collection to prevent startup crash
ENV CATALINA_OPTS="-Djdk.management.container.cgroup.disabled=true"

# Expose port 8080
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
