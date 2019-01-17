package com.vitorog.service;

import com.vitorog.domain.DroneMessage;
import com.vitorog.exception.BusinessException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

/** Service that parses the messages received from the drones. */
@Service
public class MessageParserService {

  /**
   * Receives a message and returns a {@link DroneMessage} object.
   *
   * @param message the message that will be parsed
   * @return the resulting value object ({@link DroneMessage})
   */
  public DroneMessage parse(Message message) {
    Object payLoadObj = message.getPayload();

    if (payLoadObj instanceof String) {
      String payload = (String) payLoadObj;
      String[] payloadArray = payload.split(";");

      if (payloadArray.length == 3) {
        return DroneMessage.builder()
            .droneId(Long.valueOf(payloadArray[0]))
            .latitude(Double.valueOf(payloadArray[1]))
            .longitude(Double.valueOf(payloadArray[2]))
            .timestamp(message.getHeaders().getTimestamp())
            .build();
      }
    }
    throw new BusinessException("Invalid status message");
  }
}
