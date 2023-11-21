package org.swmaestro.mohaeng.config;

public class CustomH2Functions {

    public static String createPoint(double longitude, double latitude) {
        return String.format("POINT(%f %f)", longitude, latitude);
    }

    public static double h2DistanceSphere(String point1, String point2) {
        double[] latLon1 = extractLatLonFromPoint(point1);
        double[] latLon2 = extractLatLonFromPoint(point2);
        return haversine(latLon1[1], latLon1[0], latLon2[1], latLon2[0]);
    }

    private static double[] extractLatLonFromPoint(String point) {
        String[] parts = point.split("[ ()]+");
        return new double[]{Double.parseDouble(parts[1]), Double.parseDouble(parts[2])};
    }

    private static double haversine(double lon1, double lat1, double lon2, double lat2) {
        final int R = 6371; // Radius of the earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
}
