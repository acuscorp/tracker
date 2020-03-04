package com.acuscorp.googlemaps;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public GPS(@NonNull String gpsName, double latitude, double longitude, double speed, double distance, String timeTraveled) {


        this.gpsName = gpsName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.distance = distance;
        this.timeTraveled = timeTraveled;



    }

    @NonNull
    public String getGpsName() {
        return gpsName;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDistance() {
        return distance;
    }

    public void setId(int id) {
        this.id = id;
    }

 
    public String getTimeTraveled() {
        return timeTraveled;
    }

    public void setTimeTraveled(@NonNull String timeTraveled) {
        this.timeTraveled = timeTraveled;
    }
}
