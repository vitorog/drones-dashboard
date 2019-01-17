package com.vitorog.configuration;

import static java.util.logging.Logger.getLogger;

import com.vitorog.constant.DroneServerConstants;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

/** Bean that configures MQTT input connection from the MQTT broker. */
@Configuration
public class MQTTConfig {

  private static final Logger logger = getLogger(MQTTConfig.class.getName());

  private static final int COMPLETION_TIMEOUT = 5000;
  private static final int QOS_LEVEL = 1;

  @Value("${mqtt.server}")
  private String mqttServer;

  @Bean
  public MessageChannel mqttInputChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageProducer inbound() {
    logger.info("Initializing MQTT adapter...");

    logger.log(Level.INFO, "Connecting to broker server: {0}", mqttServer);
    MqttPahoMessageDrivenChannelAdapter adapter =
        new MqttPahoMessageDrivenChannelAdapter(
            mqttServer,
            DroneServerConstants.DRONE_SERVER_CLIENT_ID,
            DroneServerConstants.DRONE_REPORT_TOPIC);
    adapter.setCompletionTimeout(COMPLETION_TIMEOUT);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(QOS_LEVEL);
    adapter.setOutputChannel(mqttInputChannel());
    return adapter;
  }
}
