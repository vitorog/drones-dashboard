package com.vitorog.repository;

import com.vitorog.domain.Drone;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

/** "in-memory" database for {@link Drone} entities. */
@Repository
public class DroneRepository {

  private Map<Long, Drone> droneDatabase = new HashMap<>();

  public void save(Drone drone) {
    Long droneId = drone.getDroneId();
    droneDatabase.put(droneId, drone);
  }

  public Collection<Drone> loadAll() {
    return droneDatabase.values();
  }
}
