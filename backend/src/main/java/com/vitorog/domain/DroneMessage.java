package com.vitorog.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DroneMessage {

  protected Long droneId;
  protected Double latitude;
  protected Double longitude;
  protected Long timestamp;
}
