package com.vitorog.util;

import static org.junit.Assert.*;

import org.junit.Test;

/** Tests for {@link GeoLocationUtils} */
public class GeoLocationUtilsTest {

  @Test
  public void calculateDistance_should_correctly_calculate() {
    // Given
    // These points are approx 4446 apart according to: https://www.nhc.noaa.gov/gccalc.shtml
    Double newLat = 50.0;
    Double newLong = 88.0;
    Double oldLat = 90.0;
    Double oldLong = 95.0;

    // When
    Double distanceInKm =
        GeoLocationUtils.calculateDistance(newLat, newLong, oldLat, oldLong) / 1000;
    double diff = Math.abs(distanceInKm - 4446.0);

    // Then
    // 100 meters precision is good enough for this test
    assertTrue(diff <= 100.0);
  }
}
