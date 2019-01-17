package com.vitorog.service;

import static java.util.logging.Logger.getLogger;

import com.vitorog.domain.Drone;
import com.vitorog.domain.DroneMessage;
import com.vitorog.repository.DroneRepository;
import com.vitorog.repository.MessageRepository;
import com.vitorog.util.GeoLocationUtils;
import com.vitorog.util.TimestampUtils;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Service that handles drone reports. It handles the messages received from the MQTT broker. The
 * messages received are parsed and then the corresponding drone status is store in the "in-memory"
 * database.
 */
@Service
@RequiredArgsConstructor
public class DroneService implements MessageHandler {

  private static final Logger logger = getLogger(DroneService.class.getName());

  private static final int MAX_SECONDS_WITHOUT_MOVING = 10;
  private static final int ONE_METER = 1;

  private final MessageParserService messageParserService;

  private final MessageRepository messageRepository;

  private final DroneRepository droneRepository;

  /**
   * Reads the messages from the MQTT broker.
   *
   * @param message the received message
   */
  @Override
  @ServiceActivator(inputChannel = "mqttInputChannel")
  public void handleMessage(Message<?> message) {
    logger.log(Level.FINEST, "Received message: {0}", message);
    DroneMessage latestMessage = messageParserService.parse(message);
    messageRepository.saveMessage(latestMessage);

    // Calculates the drone status based on the latest message and stores it in the "database"
    Optional<Drone> drone = updateDrone(latestMessage.getDroneId());
    if (drone.isPresent()) {
      droneRepository.save(drone.get());
    }
  }

  /**
   * Returns the information of all the drones
   *
   * @return a collection with all the drones information.
   */
  public Collection<Drone> getDrones() {
    return droneRepository.loadAll();
  }

  /**
   * Returns the status of a drone by Id. The status contains the latitude, longitude, speed and a
   * flag indicating if the drone is stopped
   *
   * @param droneId the id of the drone whose status will be calculated
   * @return a {@link Drone} with all drone status information
   */
  private Optional<Drone> updateDrone(Long droneId) {
    logger.log(Level.FINEST, "Updating drone with Id: {0}", droneId);
    List<DroneMessage> messages = messageRepository.loadByDroneId(droneId);

    if (messages.isEmpty()) {
      return Optional.empty();
    }

    DroneMessage latest = messages.get(0);

    Double currentSpeed = null;
    boolean hasStopped = false;

    // Calculates the speed based on the previous messages
    if (messages.size() >= 2) {
      DroneMessage previous = messages.get(1);

      double traveledDistance = calculateTraveledDistance(latest, previous);
      double numMs = TimestampUtils.getNumMsBetween(latest.getTimestamp(), previous.getTimestamp());

      // Speed in meters / second
      currentSpeed = (traveledDistance / numMs) * 1000;

      // If drone moved less than 1 meter
      if (traveledDistance <= ONE_METER) {
        DroneMessage oldest = CollectionUtils.lastElement(messages);

        // Checks if the drone has not moved more than one meter in the last seconds
        if (TimestampUtils.getNumSecondsBetween(latest.getTimestamp(), oldest.getTimestamp())
            >= MAX_SECONDS_WITHOUT_MOVING) {
          hasStopped = calculateTraveledDistance(latest, oldest) <= ONE_METER;
        }
      }
    }

    return Optional.of(
        Drone.builder()
            .droneId(droneId)
            .latitude(latest.getLatitude())
            .longitude(latest.getLongitude())
            .hasStopped(hasStopped)
            .speed(currentSpeed)
            .build());
  }

  /**
   * Calculates the drone traveled distance based on two messages. It uses {@link GeoLocationUtils}
   * to perform the proper calculations.
   *
   * @param recent a recent message for the drone
   * @param older an older message received from the drone
   * @return the distance based on the two messages
   */
  private Double calculateTraveledDistance(DroneMessage recent, DroneMessage older) {
    return GeoLocationUtils.calculateDistance(
        recent.getLatitude(), recent.getLongitude(), older.getLatitude(), older.getLongitude());
  }
}
