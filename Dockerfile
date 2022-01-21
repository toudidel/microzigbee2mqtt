FROM adoptopenjdk/openjdk11:alpine-jre
#FROM adoptopenjdk/openjdk11:armv7l-ubuntu-jre-11.0.8_10
ADD target/zigbee2mqtt-*.jar /opt/microzigbee2mqtt.jar
ADD src/main/resources/application.example.yml /opt/application.yml
EXPOSE 8080
ENV MICRONAUT_CONFIG_FILES=/opt/application.yml
ENTRYPOINT ["java", "-jar", "/opt/microzigbee2mqtt.jar"]