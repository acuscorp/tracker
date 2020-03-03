package com.acuscorp.googlemaps;

import com.google.firebase.database.Exclude;

public class Upload {
    private String gpsName;
    private double latitude;
    private double longitude;
    private double speed;
    private double distance;
    private int id;
    private String mKey;
    private String time;
    public Upload(){}

    public Upload(int id, String gpsName, double latitude, double longitude, double speed, double distance, String time) {
        this.gpsName = gpsName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.distance = distance;
        this.id = id;
        this.time =time;
    }

    public String getGpsName() {
        return gpsName;
    }

    public void setGpsName(String gpsName) {
        this.gpsName = gpsName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Exclude
    public String getmKey() {
        return mKey;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
