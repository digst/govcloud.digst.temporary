
#FROM ubuntu:16.04
# Create a directory for your MapR Application and copy the Application
#ENV HOME /root
#WORKDIR /root
FROM openjdk:8-jre-alpine

RUN mkdir -p /sit/share/mapr-apps/
RUN mkdir -p /sit/share/mapr-apps/web-inf/
RUN mkdir -p /sit/share/mapr-apps/tmpfiles/



COPY ./target/streaming-producer-1.0-SNAPSHOT-jar-with-dependencies.jar /sit/share/mapr-apps/streaming-producer-1.0-SNAPSHOT-jar-with-dependencies.jar
COPY run.sh /sit/share/mapr-apps/run.sh
COPY ./target/classes/config/producer.properties /sit/share/mapr-apps/producer.properties
COPY ./target/classes/config/topics.properties /sit/share/mapr-apps/topics.properties
COPY ./target/classes/config/application.properties /sit/share/mapr-apps/application.properties

COPY ./target/classes/web-inf/ /sit/share/mapr-apps/web-inf/


RUN chmod +x /sit/share/mapr-apps/run.sh

EXPOSE 8010/tcp

CMD ["/usr/bin/java", "-cp", "/sit/share/mapr-apps/streaming-producer-1.0-SNAPSHOT-jar-with-dependencies.jar", "app.RunApp", "/sit/share/mapr-apps/application.properties"]