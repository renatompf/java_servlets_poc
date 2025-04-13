## ====== Build Image ====== ##
FROM maven:3.9.6-amazoncorretto-21 AS build_image

WORKDIR /app

COPY pom.xml .
RUN mvn -e -B dependency:resolve

COPY src ./src
RUN mvn package -DskipTests

# Use an official Tomcat image for deployment
FROM tomcat:11.0

# Copy the built WAR file to the ROOT directory
COPY --from=build_image /app/target/servlets-poc.war /usr/local/tomcat/webapps/ROOT.war

# Expose Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]