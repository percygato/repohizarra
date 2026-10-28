FROM eclipse-temurin:17-jdk-jammy
RUN apt-get update && apt-get install -y maven curl
WORKDIR /app
COPY . .
RUN mvn clean package
RUN curl -O https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.18/bin/apache-tomcat-10.1.18.tar.gz && \
    tar -xzvf apache-tomcat-10.1.18.tar.gz && \
    rm apache-tomcat-10.1.18.tar.gz
RUN rm -rf apache-tomcat-10.1.18/webapps/* && \
    cp target/PortafolioAnimeJava.war apache-tomcat-10.1.18/webapps/ROOT.war
RUN mkdir -p /app/uploads
CMD ["./apache-tomcat-10.1.18/bin/catalina.sh", "run"]
