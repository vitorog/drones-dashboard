package com.vitorog.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Drone {

  private Long droneId;
  private Double latitude;
  private Double longitude;
  private Double speed;
  private Boolean hasStopped;
}
