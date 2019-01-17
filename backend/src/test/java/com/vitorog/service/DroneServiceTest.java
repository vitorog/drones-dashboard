package com.vitorog.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vitorog.domain.DroneMessage;
import com.vitorog.repository.MessageRepository;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

@RunWith(MockitoJUnitRunner.class)
public class DroneServiceTest {

  @Mock MessageRepository messageRepository;

  @Mock MessageParserService messageParserService;

  @InjectMocks DroneService droneService;

  @Test
  public void handleMessage_should_store_received_messages() {
    // Given
    Message<?> message = new GenericMessage<>("TEST");

    // When
    when(messageParserService.parse(Mockito.any(Message.class)))
        .thenReturn(DroneMessage.builder().droneId(0L).build());
    when(messageRepository.loadByDroneId(0L)).thenReturn(Collections.emptyList());
    droneService.handleMessage(message);

    // Then
    verify(messageRepository).saveMessage(Mockito.any(DroneMessage.class));
  }
}
