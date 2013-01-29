package eu.livotov.labs.android.robotools.geo;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 15/01/2013
 */
public class RTGeo {

    private static final double PK = 180 / 3.1415926;
    private static final double EARTH_RADIUS = 6371;

    public static double findDistance(double lat1, double lon1, double lat2, double lon2)
    {
        return Math.acos(Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * (Math.cos(Math.toRadians(lon1)) * Math.cos(Math.toRadians(lon2)) + Math.sin(Math.toRadians(lon1)) * Math.sin(Math.toRadians(lon2)))) * EARTH_RADIUS;
    }

}
