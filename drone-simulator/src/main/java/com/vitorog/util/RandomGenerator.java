package com.vitorog.util;

import java.util.Random;

/** Simple util class to generate random numbers in a given range. */
public class RandomGenerator {

  private RandomGenerator() {}

  /**
   * Generates a Double random number between min and max.
   *
   * @param min the min value
   * @param max the max value
   * @return the random number
   */
  public static Double generate(Double min, Double max) {
    return min + (max - min) * new Random().nextDouble();
  }
}
