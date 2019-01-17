FROM gradle:4.10.0-jdk8

WORKDIR /usr/src/drone-simulator

# Copy all local files into the image.
COPY . .

USER root
RUN chown -R gradle /usr/src/drone-simulator
USER gradle

RUN ./gradlew build

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "./build/libs/drone-simulator-1.0-SNAPSHOT.jar"]