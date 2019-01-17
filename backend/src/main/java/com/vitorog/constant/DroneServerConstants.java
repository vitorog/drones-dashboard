package com.vitorog.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Contains project related constants */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DroneServerConstants {
  public static final String DRONE_SERVER_CLIENT_ID = "DRONE_SERVER";
  public static final String DRONE_REPORT_TOPIC = "DRONE_STATUS";
}
