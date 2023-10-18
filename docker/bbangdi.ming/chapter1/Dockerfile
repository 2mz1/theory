#FROM ubuntu:latest
#
#WORKDIR /app
#
#COPY helloworld /app
#RUN chmod +x /app/helloworld
#
#CMD ["/app/helloworld"]


FROM openjdk:17-jdk-slim

COPY helloworld.jar /app/app.jar
RUN chmod +x /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]
