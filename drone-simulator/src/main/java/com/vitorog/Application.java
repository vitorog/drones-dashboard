package com.vitorog;

import com.vitorog.domain.Drone;
import com.vitorog.util.RandomGenerator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * This application instantiates n Drones (defined in application.properties). Each Drone then
 * connects to the MQTT broker and the central server and reports position to it. Each Drone runs in
 * different thread.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

  @Value("${mqtt.server}")
  private String server;

  @Value("${simulator.num_drones}")
  private Integer numDrones;

  @Value("${mqtt.server.topic}")
  private String mqttTopic;

  @Value("${mqtt.server.qosLevel}")
  private Integer qosLevel;

  @Value("${simulator.update_freq}")
  private Integer updateFrequency;

  public static void main(String[] args) {
    new SpringApplicationBuilder(Application.class).web(WebApplicationType.NONE).run(args);
  }

  @Override
  public void run(String... args) {

    final ExecutorService service = Executors.newFixedThreadPool(numDrones);

    IntStream.range(0, numDrones)
        .parallel()
        .forEach(
            n -> {
              Drone drone =
                  Drone.builder()
                      .droneId(Long.valueOf(n))
                      .server(server)
                      .mqttTopic(mqttTopic)
                      .qosLevel(qosLevel)
                      .updateFreqInMs(updateFrequency)
                      .distanceToTravel(RandomGenerator.generate(15.0, 30.0))
                      .stoppedThreshold(RandomGenerator.generate(0.01, 0.05))
                      .moveAgainThreshold(RandomGenerator.generate(0.01, 0.05))
                      .bearing(RandomGenerator.generate(0.0, 360.0))
                      .build();

              service.submit(drone);
            });
  }
}
