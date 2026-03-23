package com.travel.util;

/**
 * 地理计算工具类。
 */
public final class GeoUtil
{

    private static final double EARTH_RADIUS_M = 6371000;

    private GeoUtil()
    {
    }

    /**
     * Haversine 公式计算两点间球面距离（米）。
     */
    public static double distanceMeters(double lat1, double lng1, double lat2, double lng2)
    {
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double dPhi = Math.toRadians(lat2 - lat1);
        double dLambda = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dPhi / 2) * Math.sin(dPhi / 2)
            + Math.cos(phi1) * Math.cos(phi2) * Math.sin(dLambda / 2) * Math.sin(dLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_M * c;
    }
}

