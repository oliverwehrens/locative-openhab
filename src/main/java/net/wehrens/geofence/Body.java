package net.wehrens.geofence;

public class Body {

    String device;
    String id;
    String latitude;
    String longitude;
    long timestamp;
    String test;

    @Override
    public String toString() {
        return "Body{" +
                "device='" + device + '\'' +
                ", id='" + id + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", timestamp=" + timestamp +
                ", test='" + test + '\'' +
                '}';
    }
}
