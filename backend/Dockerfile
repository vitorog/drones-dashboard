FROM gradle:4.10.0-jdk8

WORKDIR /usr/src/drone-server

# Copy all local files into the image.
COPY . .

USER root
RUN chown -R gradle /usr/src/drone-server
USER gradle

RUN ./gradlew build

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "./build/libs/drone-server-1.0-SNAPSHOT.jar"]