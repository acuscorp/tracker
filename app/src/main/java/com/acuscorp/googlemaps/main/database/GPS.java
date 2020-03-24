package com.acuscorp.googlemaps.main.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Noé Adrian Acuna Prado on 28/02/2020.
 *
 * acuscorp@gmail.com
 */
@Entity(tableName = "gps_table")
public class GPS {
    @NonNull
    private String gpsName;
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private double latitude;
    @NonNull
    private double longitude;
    @NonNull
    private double speed;
    @NonNull
    private double distance;
    @NonNull
    private  String timeTraveled;
    @NonNull
    private String driver;


    public GPS(@NonNull String gpsName, double latitude, double longitude, double speed, double distance, @NonNull String timeTraveled,@NonNull String driver) {
        this.gpsName = gpsName;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.distance = distance;
        this.timeTraveled = timeTraveled;
        this.driver = driver;
    }

    @NonNull
    public String getGpsName() {
        return gpsName;
    }

    public void setGpsName(@NonNull String gpsName) {
        this.gpsName = gpsName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @NonNull
    public String getTimeTraveled() {
        return timeTraveled;
    }

    public void setTimeTraveled(@NonNull String timeTraveled) {
        this.timeTraveled = timeTraveled;
    }

    @NonNull
    public String getDriver() {
        return driver;
    }

    public void setDriver(@NonNull String driver) {
        this.driver = driver;
    }
}
