package com.acuscorp.googlemaps.main;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

class GPSService implements OnMapReadyCallback {
    private static final String TAG = "GPSService";
    private final OnLocationListener onLocationListener;
    private GoogleMap mMap;
    private LocationCallback locationCallback;


    private LatLng lastLatLon;
    private PolylineOptions polylineOptions;
    private double lastSpeed;
    private long timeSatmp = 1000;
    private double maxSpeedFilter = 140;
    private double currentDistance;
    private Location lastLocation;
    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private int hoursTraveled = 0;
    private int minutesTraveled = 0;
    private int secondsTraveled = 0;
    private Context context;
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;
    private Location location;
    private boolean startPlolyline;

    public GPSService(Context context, SupportMapFragment mapFragment, OnLocationListener onLocationListener) {
        this.context = context;
        mapFragment.getMapAsync(this);
        this.onLocationListener =onLocationListener;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(timeSatmp);
        locationRequest.setSmallestDisplacement(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        initLocCallBack();


    }

    private void initLocCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = (Location) locationResult.getLastLocation();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.setMinZoomPreference(16f);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                if(lastLocation!=null) {
                    double speed = location.getSpeed();
                    double distance = location.distanceTo(lastLocation);
                    if (speed > 5 && speed < maxSpeedFilter) {
                        if (!(speed >= lastSpeed + 20)) {
                            // here is to avoid jumping
                        }
                    }
                    addDistance(distance);
                    lastSpeed = speed;
                    location.setSpeed((float) lastSpeed);
                    setLocation(location);
                    onLocationListener.onLocationChange(location);
                    if(startPlolyline) {
                        polylineOptions=null;
                        polylineOptions= new PolylineOptions().color(Color.GREEN).width(8f);
                        mMap.addPolyline(polylineOptions.add(lastLatLon).add(latLng));
                    }

                }
                lastLatLon = latLng;

                lastLocation =location;
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                Log.d(TAG, "onLocationAvailability: ");
            }
        };

        client = LocationServices.getFusedLocationProviderClient(context);
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }


    public String getTimeTraveled() {
        int hrs = Calendar.getInstance().get(Calendar.HOUR);
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        int sec = Calendar.getInstance().get(Calendar.SECOND);
        hoursTraveled = Math.abs(hrs - hours);
        minutesTraveled = Math.abs((min - minutes));
        secondsTraveled = Math.abs(sec - seconds);
        String timeFormat = hoursTraveled < 10 ? "0" + hoursTraveled : hoursTraveled + "";
        timeFormat += ":" + (minutesTraveled < 10 ? "0" + minutesTraveled : minutesTraveled);
        timeFormat += ":" + (secondsTraveled < 10 ? "0" + secondsTraveled : secondsTraveled);

        return timeFormat;

    }

    private void setCurrentTime() {

        hours = Calendar.getInstance().get(Calendar.HOUR);

        minutes = Calendar.getInstance().get(Calendar.MINUTE);

        seconds = Calendar.getInstance().get(Calendar.SECOND);
    }
    public void setAmartkerOnMap(final String ruteName) {

        startPlolyline = ruteName.contains("start")?true:false;
        client.getLastLocation().
                addOnSuccessListener(
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                Log.d(TAG, "onSuccess: Latitude" + location.getLatitude());

                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


                                mMap.addMarker(new MarkerOptions().position(latLng).title(ruteName).icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));


                                lastLocation = new Location("last location");
                                lastLocation.setLatitude(latLng.latitude);
                                lastLocation.setLongitude(latLng.longitude);
                                resetDistance();
                                setCurrentTime();

                            }
                        }
                );

    }

    private void addDistance(double distance) {
        currentDistance += distance;
    }

    public double getCurrentDistance() {
        return currentDistance * 3600 / 1000;
    }

    public void resetDistance() {
        currentDistance = 0;
    }
    public Location getLocation(){
        return location;
    }
    private void setLocation(Location location){
        this.location =location;
    }

    interface OnLocationListener {
        void onLocationChange(Location location);
    }
}