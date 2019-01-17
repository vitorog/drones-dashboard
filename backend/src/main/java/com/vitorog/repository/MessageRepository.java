package com.vitorog.repository;

import com.vitorog.domain.DroneMessage;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;

/** This class represents an "in-memory" database and should contain the drones status messages. */
@Repository
public class MessageRepository {

  private Map<Long, LinkedList<DroneMessage>> messagesDatabase = new HashMap<>();

  // Since we are not really worried about history, we can keep
  // only enough messages so that we can check if a Drone has stopped in the last 10 seconds.
  // This number should be based in the frequency of updates from the drones.
  private static final int MAX_STATUS_NUM = 11;

  /**
   * Saves a message received from a Drone.
   *
   * @param message message to be saved
   */
  public void saveMessage(DroneMessage message) {
    Long droneId = message.getDroneId();

    if (!messagesDatabase.containsKey(droneId)) {
      messagesDatabase.put(droneId, new LinkedList<>());
    }

    if (messagesDatabase.get(droneId).size() >= MAX_STATUS_NUM) {
      messagesDatabase.get(droneId).pollLast();
    }
    messagesDatabase.get(droneId).push(message);
  }

  /**
   * Loads all the messages for a given drone Id.
   *
   * @param droneId the Id of the drone whose messages will be retrieved
   * @return a list with all the messages or an empty list if no messages exist
   */
  public List<DroneMessage> loadByDroneId(Long droneId) {
    if (messagesDatabase.containsKey(droneId)) {
      return messagesDatabase.get(droneId);
    }
    return Collections.emptyList();
  }

  /**
   * Returns the Id of all the drones in the database.
   *
   * @return a set with all Ids.
   */
  public Set<Long> getDroneIds() {
    return messagesDatabase.keySet();
  }
}
