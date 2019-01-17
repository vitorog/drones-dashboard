package com.vitorog.util;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * This class contains methods related to geolocation. We are using the geodesy library
 * (https://github.com/mgavaghan/geodesy) to calculate distance between latitude and longitudes.
 */
public class GeoLocationUtils {

  private GeoLocationUtils() {}

  /**
   * Returns the distance between two sets of coordinates.
   *
   * @param latestLat most recent latitude
   * @param latestLong most recent longitude
   * @param previousLat previous latitude
   * @param previousLong previous longitude
   * @return the distance between the two coordinates in meters
   */
  public static Double calculateDistance(
      Double latestLat, Double latestLong, Double previousLat, Double previousLong) {

    GeodeticCalculator geoCalc = new GeodeticCalculator();

    Ellipsoid reference = Ellipsoid.WGS84;

    GlobalCoordinates currentCoordinates = new GlobalCoordinates(latestLat, latestLong);
    GlobalCoordinates previousCoordinates = new GlobalCoordinates(previousLat, previousLong);

    GeodeticCurve geodeticCurve =
        geoCalc.calculateGeodeticCurve(reference, currentCoordinates, previousCoordinates);

    // Returns the distance in meters
    return geodeticCurve.getEllipsoidalDistance();
  }
}
