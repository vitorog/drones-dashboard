package com.vitorog.util;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.Test;

/** Tests for {@link TimestampUtils} */
public class TimestampUtilsTest {

  @Test
  public void getNumSecondsBetween_should_correctly_calculate() {
    // Given
    Long time1 = LocalDateTime.of(2018, 9, 9, 10, 0, 10).toInstant(ZoneOffset.UTC).toEpochMilli();
    Long time2 = LocalDateTime.of(2018, 9, 9, 10, 0, 20).toInstant(ZoneOffset.UTC).toEpochMilli();

    // When
    Long numSeconds = TimestampUtils.getNumSecondsBetween(time2, time1);

    // Then
    assertEquals(Long.valueOf(10), numSeconds);
  }
}
