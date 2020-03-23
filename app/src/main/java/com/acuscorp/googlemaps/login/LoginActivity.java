package com.acuscorp.googlemaps.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.acuscorp.googlemaps.MainActivity;
import com.acuscorp.googlemaps.R;
import com.acuscorp.googlemaps.Route;
import com.acuscorp.googlemaps.SelectRouteActivity;

public class LoginActivity extends AppCompatActivity {
  private static final String TAG = "LoginActivity";
  private EditText et_userName;
  private EditText et_password;
  private Button btn_login;
  private String _username="admin", _password="admin";
  private  String username,password;
  private final int LOCATION_PERMISSION = 999;
  private final String user = "com.acuscorp.googlemaps.login.user";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);
    btn_login = findViewById(R.id.btn_login);
    et_userName =findViewById(R.id.et_user_name);
    et_password = findViewById(R.id.et_password);
    btn_login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        username = et_userName.getText().toString();
        password = et_password.getText().toString();

        if(username.isEmpty()){
          Toast.makeText(LoginActivity.this, "Please type your username", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
          Toast.makeText(LoginActivity.this, "Please type you password", Toast.LENGTH_SHORT).show();
        }else if(username.equals(_username)){

          if(password.equals(_password)){
            Intent intent = new Intent(getApplicationContext(), SelectRouteActivity.class);
            intent.putExtra(user,username);
            startActivity(intent);
          }
          else {
            Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
          }
        } else{
          Toast.makeText(LoginActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == LOCATION_PERMISSION && permissions.length > 0) {
      if (permissions[0].contains("ACCESS_FINE_LOCATION"))
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      } else {
        Toast.makeText(this, "Please provide location permission to aneable application", Toast.LENGTH_SHORT).show();
      }
    }

  }

}
