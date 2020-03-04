package com.acuscorp.googlemaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {
    //public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
//        GoogleMap.OnMyLocationClickListener{
    private static final String TAG = "MainActivity";

    private static final int LOCATION_PERMISSION = 999;
    public static final int DELETE_DATA = 789;
    private GoogleMap mMap;

    private GPSViewModel gpsViewModel;
    private States states;
    private View popupInputDialogView = null;
    private FusedLocationProviderClient client;
    private LocationCallback locationCallback;
    private boolean locationStarted = false;
    private Button button;
    private String ruteName;
    private LatLng lastLatLon;
    private PolylineOptions polylineOptions;
    private double lastSpeed;
    private long timeSatmp =1000;
    private double maxSpeedFilter=140;
    private double currentDistance;
    private Location lastLocation;
    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private int hoursTraveled = 0;
    private int minutesTraveled = 0;
    private int secondsTraveled = 0;

    public enum States {
        NO_STARTED(0),
        STARTED(1),
        FINISHED(2);

        States(int i) {
            this.type = i;
        }

        private int type;

        public int getNumericType() {
            return type;
        }
    }

    public double getCurrentDistance() {
        return currentDistance*3600/1000;
    }
    public void resetDistance()
    {
        currentDistance=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);


        states = States.NO_STARTED;


        final RecyclerView recyclerView = findViewById(R.id.recycle_view_gps_data);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setHasFixedSize(true);
        final GPSAdapter gpsAdapter = new GPSAdapter();
        recyclerView.setAdapter(gpsAdapter);
        gpsViewModel = new ViewModelProvider(this).get(GPSViewModel.class);
        gpsViewModel.getAllGPSdata().observe(this, new Observer<List<GPS>>() {
            @Override
            public void onChanged(List<GPS> gps) {
                gpsAdapter.submitList(gps);
                recyclerView.scrollToPosition(gpsAdapter.getItemCount()-1);

            }
        });

        gpsAdapter.setOnItemClickListener(new GPSAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GPS gps) {
                Intent intent = new Intent(MainActivity.this, ShowDataActivity.class);
                intent.putExtra(ShowDataActivity.EXTRA_ID,gps.getId());
                intent.putExtra(ShowDataActivity.EXTRA_ROUTE_NAME,gps.getGpsName());
                intent.putExtra(ShowDataActivity.EXTRA_DISTANCE,gps.getDistance());
                intent.putExtra(ShowDataActivity.EXTRA_TIME,gps.getTimeTraveled());
                intent.putExtra(ShowDataActivity.EXTRA_LATITUDE,gps.getLatitude());
                intent.putExtra(ShowDataActivity.EXTRA_LONGITUDE,gps.getLongitude());
                startActivityForResult(intent, DELETE_DATA);



            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(timeSatmp);
//        locationRequest.setFastestInterval(5000);
        locationRequest.setSmallestDisplacement(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (states) {

                    case NO_STARTED:
                        polylineOptions =null;

                        locationStarted = true;
                        button.setText("Click again for Register your route");
                        states = States.STARTED;


                        break;

                    case STARTED:
                        polylineOptions = new PolylineOptions().color(Color.GREEN).width(8f);
                        alertDialog(MainActivity.this);
                        button.setText("Click again to finish");
                        states = States.FINISHED;

                        break;
                    case FINISHED:
                        button.setText("start");
                        locationStarted= false;
                        setAmartkerOnMap();
                        resetDistance();
                        states = States.NO_STARTED;

                        break;
                    default:

                        break;

                }
            }

            private void alertDialog(Context context) {
                // Get layout inflater object.
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

                // Inflate the popup dialog from a layout xml file.
                popupInputDialogView = layoutInflater.inflate(R.layout.promtp, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("AusCorp");
                alertDialogBuilder.setIcon(R.drawable.ic_save);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setView(popupInputDialogView);
                final EditText userInput = (EditText) popupInputDialogView
                        .findViewById(R.id.editTextDialogUserInput);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        ruteName = userInput.getText().toString();
                                        setAmartkerOnMap();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }

            private void setAmartkerOnMap() {

                client.getLastLocation().
                        addOnSuccessListener(
                                new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        Log.d(TAG, "onSuccess: Latitude" + location.getLatitude());
                                        Toast.makeText(MainActivity.this, location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        Toast.makeText(getApplicationContext(), "Location has changed   " + location.getLatitude(), Toast.LENGTH_SHORT).show();
                                        if(states==States.FINISHED){
                                            mMap.addMarker(new MarkerOptions().position(latLng).title(ruteName).icon(BitmapDescriptorFactory
                                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                            polylineOptions.color(Color.RED);

                                            lastLocation = new Location("last location");
                                            lastLocation.setLatitude(latLng.latitude);
                                            lastLocation.setLongitude(latLng.longitude);
                                            resetDistance();
                                            setCurrentTime();




                                        }else {
                                            mMap.addMarker(new MarkerOptions().position(latLng).title(ruteName).icon(BitmapDescriptorFactory
                                                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                            ruteName="";
                                            polylineOptions.color(Color.GREEN);
                                            resetDistance();
                                        }
                                        String timeTraveled =  getTimeTraveled();

                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                        mMap.setMinZoomPreference(16f);
                                        GPS gps = new GPS(ruteName, location.getLatitude(), location.getLongitude(), location.getSpeed(), getCurrentDistance(),timeTraveled);
                                        gpsViewModel.insert(gps);




                                    }
                                }
                        );

            }
        });


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = (Location) locationResult.getLastLocation();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.setMinZoomPreference(16f);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        if (locationStarted) {

                            double speed = location.getSpeed();
                            double distance = location.distanceTo(lastLocation);
                            if (speed > 5 && speed < maxSpeedFilter) {
                                if (!(speed >= lastSpeed + 20)) {
                                    // here is to avoid jumping
                                }
                            }

                            addDistance(distance);
                            lastSpeed = speed;

                            String defaultName = "default";
                            String timeTraveled =  "00:00:00";
                            if (ruteName != "" && ruteName != null) {
                                defaultName = ruteName;
                                timeTraveled =  getTimeTraveled();
                                if(polylineOptions!= null)
                                mMap.addPolyline( polylineOptions.add(lastLatLon).add(latLng));
                            }


                            GPS gps = new GPS(defaultName, location.getLatitude(), location.getLongitude(), lastSpeed, getCurrentDistance(),timeTraveled);
                            gpsViewModel.insert(gps);

                            Toast.makeText(MainActivity.this, "Location change", Toast.LENGTH_SHORT).show();

                        }


                lastLatLon = latLng;
                lastLocation = location;


            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                Log.d(TAG, "onLocationAvailability: true");
            }
        };
        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        client.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.myLooper());


    }

    private String getTimeTraveled() {
            int hrs = Calendar.getInstance().get(Calendar.HOUR);
            int min = Calendar.getInstance().get(Calendar.MINUTE);
            int sec = Calendar.getInstance().get(Calendar.SECOND);
            hoursTraveled = Math.abs(hrs-hours);
            minutesTraveled = Math.abs((min-minutes));
            secondsTraveled = Math.abs(sec - seconds);
//        if(hours<hrs){
//////            hoursTraveled = (12-hours)+hrs;
//////        }else {
//////            hoursTraveled = hrs -hours;
//////        }
//////        if(minutes<min){
//////            minutesTraveled = (60-minutes)+min;
//////        }else {
//////            minutesTraveled = min - minutes;
//////        }
//////        if(seconds<sec){
//////            secondsTraveled = (60-seconds)+sec;
//////        }else {
//////            secondsTraveled = sec - seconds;
//////        }
        String timeFormat= hoursTraveled<10?"0"+hoursTraveled:hoursTraveled+ "";
        timeFormat += ":" + (minutesTraveled<10?"0"+minutesTraveled:minutesTraveled);
        timeFormat +=":" + (secondsTraveled<10?"0"+secondsTraveled:secondsTraveled);


        return timeFormat;

    }

    private void setCurrentTime() {

        hours = Calendar.getInstance().get(Calendar.HOUR);

        minutes = Calendar.getInstance().get(Calendar.MINUTE);

        seconds = Calendar.getInstance().get(Calendar.SECOND);
    }

    private void addDistance(double distance) {
        currentDistance += distance;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION && permissions.length > 0) {
            if (permissions[0].contains("ACCESS_FINE_LOCATION"))
                Log.d(TAG, "onRequestPermissionsResult: " + permissions[0]);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

//                        GPSService gpsService = new GPSService(getApplicationContext());

            } else {
                Toast.makeText(this, "Please provide location permission to aneable application", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DELETE_DATA && resultCode == RESULT_OK){
            int id = data.getIntExtra(ShowDataActivity.EXTRA_ID,-1);
            if(id==-1){
                Toast.makeText(this, "Data can't be deleted", Toast.LENGTH_SHORT).show();
                return;
            }
            String routeName = data.getStringExtra(ShowDataActivity.EXTRA_ROUTE_NAME);
            double distance = data.getDoubleExtra(ShowDataActivity.EXTRA_DISTANCE,-1);

            double latitude = data.getDoubleExtra(ShowDataActivity.EXTRA_LATITUDE,-1);
            double longitude = data.getDoubleExtra(ShowDataActivity.EXTRA_LONGITUDE,-1);

            double speed = data.getDoubleExtra(ShowDataActivity.EXTRA_SPEED,-1);
            String timeTravled = data.getStringExtra(ShowDataActivity.EXTRA_TIME);

            GPS gps = new GPS(routeName,latitude,longitude,speed,distance, timeTravled);
            gps.setId(id);




            gpsViewModel.delete(gps);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }

    }
}
