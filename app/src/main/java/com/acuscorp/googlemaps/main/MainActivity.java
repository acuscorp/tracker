package com.acuscorp.googlemaps.main;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.acuscorp.googlemaps.R;
import com.acuscorp.googlemaps.routes.Route;
import com.acuscorp.googlemaps.util.Utils;
import com.acuscorp.googlemaps.main.database.GPS;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GPSService.OnLocationListener {
    //public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
//        GoogleMap.OnMyLocationClickListener{
    private static final String TAG = "MainActivity";

    private GPSViewModel gpsViewModel;
//    private GPSAdapter gpsAdapter;
    private Context context;
    private States states;
    private GPSService gpsService;
    private View popupInputDialogView;
    private String routeName="";
    private EditText userInput;
    private String username;
    private int userID;
    private final Utils utils = Utils.getInstance();




    public enum States {
        NO_STARTED(0),
        SAVE_DATA(1);

        States(int i) {
            this.type = i;
        }

        private int type;

        public int getNumericType() {
            return type;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();


        username = intent.getStringExtra(utils.getEXTRA_USER());
        userID = intent.getIntExtra(utils.getEXTRA_USER_ID(),0);
        Route routeName = (Route) intent.getSerializableExtra(utils.getEXTRA_ROUTE_NAME());
        this.context = this;
        init();
        setUpUI();
        startGPSService();
    }

    private void init() {
//        gpsAdapter = new GPSAdapter();
        gpsViewModel = new ViewModelProvider(this).get(GPSViewModel.class);
        gpsViewModel.getAllGPSdata().observe(this, new Observer<List<GPS>>() {
            @Override
            public void onChanged(List<GPS> gpsList) {
                if(gpsList.size()>0)
                {
                    String strGps="";
                    TextView showData = findViewById(R.id.tv_show_data);

                    strGps+="Driver: " + gpsList.get(gpsList.size()-1).getDriver();
                    strGps+="\n Latitude: " + gpsList.get(gpsList.size()-1).getLatitude();
                    strGps+="\n Longitude: " + gpsList.get(gpsList.size()-1).getLongitude();
                    strGps+= "\n size" + gpsList.size();


                    showData.setText(strGps);
                }
                //here goes the function to send data to the server
            }
        });
    }
    private void setUpUI() {
        states = States.NO_STARTED;

        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        popupInputDialogView = layoutInflater.inflate(R.layout.promtp, null);


        userInput = (EditText) popupInputDialogView
                .findViewById(R.id.editTextDialogUserInput);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  switch (states) {

                      case NO_STARTED:
                          button.setText("finish");
                          gpsService.resetDistance();
                          gpsService.setAmartkerOnMap("start " + utils.getEXTRA_ROUTE_NAME());
                          states = States.SAVE_DATA;
                          break;
                      case SAVE_DATA:
                          button.setText("start");
                          gpsService.setAmartkerOnMap("end " + utils.getEXTRA_ROUTE_NAME());
                          insertDataToDB();
                          states = States.NO_STARTED;

                          break;
                      default:

                          break;

                  }
              }}
        );

        Button btnShowDBSize = findViewById(R.id.btn_show_data);


        btnShowDBSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsViewModel.deletAllNotes();
            }
        });

    }

    private void startGPSService() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        gpsService = new GPSService(context, mapFragment, this);
    }

    private void insertDataToDB() {
        Location location = gpsService.getLocation();
        GPS gps  = new GPS("default",location.getLatitude(),location.getLongitude(),
                location.getSpeed(),gpsService.getCurrentDistance(),gpsService.getTimeTraveled(),username);
        gpsViewModel.insert(gps);
    }

    @Override
    public void onLocationChange(Location location) {

        if(states == States.SAVE_DATA&&!utils.getEXTRA_ROUTE_NAME().isEmpty()){
            GPS gps  = new GPS(utils.getEXTRA_ROUTE_NAME(),location.getLatitude(),location.getLongitude(),
                    location.getSpeed(),gpsService.getCurrentDistance(),gpsService.getTimeTraveled(),username);
            gpsViewModel.insert(gps);
            Log.d(TAG, "onLocationChange: Driver" + username + "\n distance  "+ gps.getDistance());

        }
    }


}

