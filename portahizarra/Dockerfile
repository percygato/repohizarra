FROM eclipse-temurin:17-jdk-jammy
RUN apt-get update && apt-get install -y maven curl unzip
WORKDIR /app
COPY . .
RUN mvn clean package
RUN curl -O https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.18/bin/apache-tomcat-10.1.18.tar.gz && \
    tar -xzvf apache-tomcat-10.1.18.tar.gz && \
    rm apache-tomcat-10.1.18.tar.gz
RUN rm -rf apache-tomcat-10.1.18/webapps/* && \
    mkdir -p apache-tomcat-10.1.18/webapps/ROOT && \
    unzip -q target/PortafolioAnimeJava.war -d apache-tomcat-10.1.18/webapps/ROOT && \
    mkdir -p apache-tomcat-10.1.18/webapps/ROOT/uploads && \
    cp -r uploads/* apache-tomcat-10.1.18/webapps/ROOT/uploads/ 2>/dev/null || true
CMD ["./apache-tomcat-10.1.18/bin/catalina.sh", "run"]