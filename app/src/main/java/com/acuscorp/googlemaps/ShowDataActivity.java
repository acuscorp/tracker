package com.acuscorp.googlemaps;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class ShowDataActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.acuscorp.googlemaps.EXTRA_ID";
    public static final String EXTRA_ROUTE_NAME = "com.acuscorp.googlemaps.EXTRA_ROUTE_NAME";
    public static final String EXTRA_DISTANCE = "com.acuscorp.googlemaps.EXTRA_DISTANCE";
    public static final String EXTRA_TIME = "com.acuscorp.googlemaps.EXTRA_TIME";
    public static final String EXTRA_LATITUDE = "com.acuscorp.googlemaps.EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "com.acuscorp.googlemaps.EXTRA_LONGITUDE";
    public static final String EXTRA_SPEED = "com.acuscorp.googlemaps.EXTRA_SPEED";

    private StorageReference mStorageRef;
    private DatabaseReference mDatabeseRef;
    private StorageTask mUploadTask;
    private int id;
    private String gpsName;
    private double distance;
    private double latitude;
    private double longitude;
    private double speed;
    private String timeStamp;

    private TextView tv_route_name, tv_distance, tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);


        tv_route_name = findViewById(R.id.tv_route_name_set);
        tv_distance = findViewById(R.id.tv_distance_set);
        tv_time = findViewById(R.id.tv_time_set);

        Button btn_share = findViewById(R.id.btn_share);

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(ShowDataActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadData();
                }
            }
        });
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent data  =getIntent();
        if(data.hasExtra(EXTRA_ID)){
            setTitle("Details View");


            id = getIntent().getIntExtra(EXTRA_ID,-1);
            gpsName = data.getStringExtra(ShowDataActivity.EXTRA_ROUTE_NAME);

            distance = data.getDoubleExtra(ShowDataActivity.EXTRA_DISTANCE,-1);


            latitude = data.getDoubleExtra(ShowDataActivity.EXTRA_LATITUDE,-1);

            longitude = data.getDoubleExtra(ShowDataActivity.EXTRA_LONGITUDE,-1);
            timeStamp = data.getStringExtra(EXTRA_TIME);

            speed = data.getDoubleExtra(ShowDataActivity.EXTRA_SPEED,-1);
            tv_route_name.setText(gpsName);
            tv_distance.setText(distance+"");
            tv_time.setText(timeStamp);

        }else setTitle("Add Note");
    }
    private void uploadData() {
        if(gpsName != ""){

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("gps_data");


            Upload upload = new Upload(id,gpsName,latitude,longitude,speed,distance,timeStamp);
            String uploadId = myRef.push().getKey();
            myRef.child(uploadId).setValue(upload);
            Toast.makeText(this, "data sent to firebase", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_data,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_gps_data:
                deleteData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void deleteData() {

        Intent data = new Intent();

        if(id != -1){
            if(id==-1){
                Toast.makeText(this, "Data can't be deleted", Toast.LENGTH_SHORT).show();
                return;
            }
            data.putExtra(EXTRA_ID,id);
            data.putExtra(EXTRA_ROUTE_NAME,getIntent().getStringExtra(EXTRA_ROUTE_NAME));
            data.putExtra(EXTRA_LATITUDE,getIntent().getDoubleExtra(EXTRA_LATITUDE,-1));
            data.putExtra(EXTRA_LONGITUDE,getIntent().getDoubleExtra(EXTRA_LONGITUDE,-1));
            data.putExtra(EXTRA_DISTANCE,getIntent().getDoubleExtra(EXTRA_DISTANCE,-1));
            data.putExtra(EXTRA_SPEED,getIntent().getDoubleExtra(EXTRA_SPEED,-1));
            data.putExtra(EXTRA_TIME,getIntent().getStringExtra(EXTRA_TIME));        }
        setResult(RESULT_OK,data);

        finish();
    }
}
