package com.acuscorp.googlemaps.util;

public class Utils {
  private final String EXTRA_USER = "com.acuscorp.googlemaps.login.EXTRA_USER";
  private final String EXTRA_USER_ID = "com.acuscorp.googlemaps.login.EXTRA_USER_ID";
  private final String EXTRA_ROUTE_NAME = "com.acuscorp.googlemaps.login.route_name";
  private final String EXTRA_OBJECT_ROUTE = "com.acuscorp.googlemaps.login.EXTRA_OBJERT_RESULT";

  public String getEXTRA_OBJECT_ROUTE() {
    return EXTRA_OBJECT_ROUTE;
  }

  private static Utils instance;

  public static Utils getInstance(){
    if(instance==null){
      instance= new Utils();
    }
    return instance;
  }

  public String getEXTRA_USER() {
    return EXTRA_USER;
  }

  public String getEXTRA_USER_ID() {
    return EXTRA_USER_ID;
  }

  public String getEXTRA_ROUTE_NAME() {
    return EXTRA_ROUTE_NAME;
  }
}

