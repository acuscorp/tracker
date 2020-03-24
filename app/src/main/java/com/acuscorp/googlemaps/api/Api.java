package com.acuscorp.googlemaps.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;


public interface Api {
  @GET("v1/public/characters")
  Call<DriversModel> getAllDrivers(@QueryMap Map<String,String> parameters);

}
