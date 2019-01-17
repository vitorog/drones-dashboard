package com.vitorog.domain;

import static java.util.logging.Logger.getLogger;

import com.vitorog.util.RandomGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Builder;
import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * This class represents a Drone in the simulation. It moves in a random direction with a random
 * speed and can also stop sometimes and reports its status to the central server.
 *
 * <p>It does not actually use geo-location, but it is still useful to test the drone server.
 */
@Builder
@Data
public class Drone implements Runnable {

  private static final Logger logger = getLogger(Drone.class.getName());

  // Unique drone Id sent to the central server
  @Builder.Default private Long droneId;

  // Seattle lat and long
  @Builder.Default private Double latitude = 47.608013;
  @Builder.Default private Double longitude = -122.335167;
  @Builder.Default private Double bearing = 51.7679;
  // Distance to travel until next update
  @Builder.Default private Double distanceToTravel = 20.0;
  @Builder.Default private Double speed = 0.0;

  // Thresholds used to randomly stop / move again the drone
  private Double stoppedThreshold;
  private Double moveAgainThreshold;

  // Connection parameters
  private final String server;
  private final String mqttTopic;
  private final int qosLevel;
  // Update frequency in ms
  private final int updateFreqInMs;
  private MqttClient mqttClient;

  /** Drone execution loop: simulates a movement and reports the status to the server. */
  @Override
  public void run() {
    try {
      connect();
    } catch (MqttException | InterruptedException e) {
      logger.severe("Failed to connect to MQTT broker");
    }

    boolean stopped = false;

    boolean isRunning = true;
    while (isRunning) {
      try {
        stopped = simulateMovement(stopped);
        reportStatus();

        Thread.sleep(updateFreqInMs);
      } catch (MqttException | InterruptedException e) {
        logger.log(Level.SEVERE, "Failed to publish Drone status", e);
        isRunning = false;
        Thread.currentThread().interrupt();
      }
    }

    disconnect();
  }

  /**
   * Simulates the drone movement. The drone can also stop / start moving again based on the
   * specified thresholds.
   *
   * @param stopped indicates if the drone is currently stopped
   * @return a boolean indicating if the drone stopped or is moving
   */
  private boolean simulateMovement(boolean stopped) {
    if (!stopped) {
      boolean willStop = Math.random() < stoppedThreshold;
      if (willStop) {
        logger.log(Level.INFO, "Drone {0} stopped!", droneId);

        // Some really low distance to simulate the drone not moving more than 1m
        distanceToTravel = RandomGenerator.generate(0.001, 0.005);
        stopped = true;
      }
    } else {
      boolean willMoveAgain = Math.random() < moveAgainThreshold;
      if (willMoveAgain) {
        logger.log(Level.INFO, "Drone {0} moved again!", droneId);
        distanceToTravel = RandomGenerator.generate(15.0, 30.0);
        stopped = false;
      }
    }

    // Example from:
    // https://github.com/mgavaghan/geodesy/blob/master/src/example/org/gavaghan/geodesy/example/Example.java
    GeodeticCalculator geoCalc = new GeodeticCalculator();

    Ellipsoid reference = Ellipsoid.WGS84;

    GlobalCoordinates currentCords = new GlobalCoordinates(latitude, longitude);

    double[] endBearing = new double[1];
    GlobalCoordinates dest =
        geoCalc.calculateEndingGlobalCoordinates(
            reference, currentCords, bearing, distanceToTravel, endBearing);

    this.bearing = endBearing[0];

    this.latitude = dest.getLatitude();
    this.longitude = dest.getLongitude();

    // Meters per second
    speed = (distanceToTravel / updateFreqInMs) * 1000;

    return stopped;
  }

  /**
   * Reports the current drone status to the central server.
   *
   * @throws MqttException
   */
  private void reportStatus() throws MqttException {
    String content = droneId + ";" + latitude.toString() + ";" + longitude.toString();
    MqttMessage message = new MqttMessage(content.getBytes());
    message.setQos(qosLevel);
    mqttClient.publish(mqttTopic, message);
  }

  /**
   * Connects the Drone to the specified broker.
   *
   * @throws MqttException
   */
  private void connect() throws MqttException, InterruptedException {
    MemoryPersistence persistence = new MemoryPersistence();
    mqttClient = new MqttClient(server, String.valueOf(droneId), persistence);
    MqttConnectOptions connOpts = new MqttConnectOptions();
    connOpts.setCleanSession(true);
    while (!mqttClient.isConnected()) {
      try {
        mqttClient.connect(connOpts);
      } catch (MqttException e) {
        logger.log(Level.WARNING, "Could not connect to broker. Retrying...", e);
        Thread.sleep(5000);
      }
    }
    logger.log(Level.INFO, "Drone {0} connected to broker: {1}", new Object[] {droneId, server});
  }

  /** Disconnects the Drone from the broker. */
  private void disconnect() {
    if (mqttClient.isConnected()) {
      try {
        mqttClient.disconnect();
      } catch (MqttException e) {
        logger.log(Level.SEVERE, "Failed to disconnect", e);
      }
    }
  }
}
