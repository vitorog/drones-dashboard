package com.vitorog.util;

/** Utility class to manipulate timestamps. */
public class TimestampUtils {

  private TimestampUtils() {}

  /**
   * Calculates the number of seconds between two timestamps
   *
   * @param newTimestamp most recent timestamp
   * @param oldTimestamp older timestamp
   * @return the number of seconds between the timestamps
   */
  public static Long getNumSecondsBetween(Long newTimestamp, Long oldTimestamp) {
    return getNumMsBetween(newTimestamp, oldTimestamp) / 1000;
  }

  /**
   * Calculates the number of milliseconds between two timestamps
   *
   * @param newTimestamp most recent timestamp
   * @param oldTimestamp older timestamp
   * @return the number of milliseconds between the timestamp
   */
  public static Long getNumMsBetween(Long newTimestamp, Long oldTimestamp) {
    return (newTimestamp - oldTimestamp);
  }
}
