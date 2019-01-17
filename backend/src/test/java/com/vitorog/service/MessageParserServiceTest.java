package com.vitorog.service;

import static junit.framework.TestCase.assertEquals;

import com.vitorog.domain.DroneMessage;
import com.vitorog.exception.BusinessException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

/** Tests for the MessageParserServiceTest */
public class MessageParserServiceTest {

  private MessageParserService parserService;

  @Before
  public void setUp() {
    parserService = new MessageParserService();
  }

  @Test
  public void parse_should_return_drone_status() {
    // Given
    // Message format is: drone id;latitude;longitude
    Message<?> message = new GenericMessage<>("1;0.5;1.0");

    // When
    DroneMessage status = parserService.parse(message);

    // Then
    assertEquals("Drone Id", Long.valueOf(1), status.getDroneId());
    assertEquals("Latitude", 0.5, status.getLatitude());
    assertEquals("Longitude", 1.0, status.getLongitude());
  }

  @Test(expected = BusinessException.class)
  public void parse_should_throw_exception_if_invalid_message() {
    // Given
    Message<?> message = new GenericMessage<>("INVALID_MESSAGE");

    // When
    parserService.parse(message);
  }
}
