# README #

## Drone Control Dashboard

### Introduction

This project implements a dashboard to track drones positions in real-time.
It consists of 3 modules:
frontend (dashboard), backend (central server) and drone-simulator.

To run the project, execute the following command in the root folder:
```
docker-compose up
```

Docker will run all 3 modules as well as the required VerneMQ (https://vernemq.com/) MQTT broker.
The dashboard will be accessible in http://localhost:8080

It was created for a programming challenge and as a learning experience.

### Project and Modules description

#### VerneMQ broker
The message broker. The drones will send messages to its queue and the central-server will read them from it.
It supports the MQTT protocol, a lightweight protocol, suitable for IoT devices.

#### Drone-Simulator
This application generates a few "fake drones" that report their position periodically. 
The drones have a random chance to "stop moving", so that we can test if
the detection of "stopped" drones is working properly. They also have a random chance to start moving again.
By default, 10 drones are created in the same coordinates (latitude and longitude), but with a random bearing.

#### Drone-Server
The main application, it reads the updates from the MQTT broker and stores the data to an "in-memory" fake database. 
It also calculates the drones speed based on their position and flags any drone that has not moved more than 1 meter in the last 10 seconds.
The server can send the data via a websocket.

#### Dashboard 
It connects to the central server websocket and receives the drone information through it. The dashboard
consists of a table with the drones information and drones that are "stopped" have a visual marking.

### Stack

For this project, the following language/tools/frameworks were used:

* Frontend
	* React
	* create-react-app
	* Reactstrap
	* Prettier (a code formatter)
	* sockjs-client and stomp-websocket (for websocket communication)
	
* Backend and Drone-Simulator
	* Java 8
	* Spring and SpringBoot (and modules such as MQTT, Websocket)	
	* Gradle (as the build system tool)
	* JUnit and Mockito (for unit tests and mocking)
	* Spotless java (a code formatter / enforcer - https://github.com/diffplug/spotless)
	* Lombok (a tool that improves java code writing - https://projectlombok.org/)
	* Geodesy (a Java library to perform operations with latitude and longitudes - https://github.com/mgavaghan/geodesy) 
	
* Other tools:
	* VerneMQ (MQTT broker)
	* Docker and Docker-Compose
	* IntelliJ IDEA IDE (both frontend and backend)
	* Git
	* SonarLint (code quality tool - https://www.sonarlint.org/)
