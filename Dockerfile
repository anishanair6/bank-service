FROM openjdk:8-jdk-alpine
#RUN addgroup -S demo && adduser -S demo -G demo
#USER demo:demo
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app.jar"]