package com.vitorog.controller;

import com.vitorog.service.DroneService;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

/** WebSocket controller that sends the drones status every 1 second. */
@Controller
@RequiredArgsConstructor
@EnableScheduling
public class DroneController {

  private static final Logger logger = Logger.getLogger(DroneController.class.getName());

  private final DroneService droneService;

  private final SimpMessagingTemplate template;

  @Scheduled(fixedRate = 1000)
  public void sendDronesUpdate() {
    logger.log(Level.FINE, "Sending drones update...");
    this.template.convertAndSend("/topic/drones", droneService.getDrones());
  }
}
