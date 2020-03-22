package com.acuscorp.googlemaps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GPSService.OnLocationListener {
    //public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
//        GoogleMap.OnMyLocationClickListener{
    private static final String TAG = "MainActivity";

    private GPSViewModel gpsViewModel;
    private GPSAdapter gpsAdapter;
    private Context context;
    private States states;
    private GPSService gpsService;
    private View popupInputDialogView;
    private String routeName="";
    private EditText userInput;



    public enum States {
        NO_STARTED(0),
        STARTED(1),
        SAVE_DATA(2);

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
        this.context = this;
        init();
        setUpUI();
        startGPSService();
    }

    private void init() {
        gpsAdapter = new GPSAdapter();
        gpsViewModel = new ViewModelProvider(this).get(GPSViewModel.class);
        gpsViewModel.getAllGPSdata().observe(this, new Observer<List<GPS>>() {
            @Override
            public void onChanged(List<GPS> gps) {
                gpsAdapter.submitList(gps);
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

                          gpsService.resetDistance();
                          button.setText("Click again for Register your route");
                          states = States.STARTED;

                          insertDataToDB();


                          break;

                      case STARTED:
                          button.setText("Click again to finish");
                          alertDialog();
                          states = States.SAVE_DATA;

                          break;
                      case SAVE_DATA:
                          button.setText("start");
                          gpsService.setAmartkerOnMap(routeName);
                          insertDataToDB();
                          states = States.NO_STARTED;

                          break;
                      default:

                          break;

                  }
              }}
        );

    }

    private void startGPSService() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        gpsService = new GPSService(context, mapFragment, this);
    }

    private void insertDataToDB() {
        Location location = gpsService.getLocation();
        GPS gps  = new GPS("default",location.getLatitude(),location.getLongitude(),
                location.getSpeed(),gpsService.getCurrentDistance(),gpsService.getTimeTraveled());
        gpsViewModel.insert(gps);
    }



    private void alertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("AusCorp");
        alertDialogBuilder.setIcon(R.drawable.ic_save);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setView(popupInputDialogView);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                routeName = userInput.getText().toString();
                                gpsService.setAmartkerOnMap(routeName);
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

    @Override
    public void onLocationChange(Location location) {

        if(states == States.SAVE_DATA&&!routeName.isEmpty()){
            GPS gps  = new GPS(routeName,location.getLatitude(),location.getLongitude(),
                    location.getSpeed(),gpsService.getCurrentDistance(),gpsService.getTimeTraveled());
            gpsViewModel.insert(gps);

        }
    }


}

