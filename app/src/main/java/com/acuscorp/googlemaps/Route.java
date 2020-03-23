package com.acuscorp.googlemaps;

public class Route {
  private int id;
  private String routeName;
  private double latitude;
  private double longitude;

  public Route(int id, String routeName, double latitude, double longitude) {
    this.id = id;
    this.routeName = routeName;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getRouteName() {
    return routeName;
  }

  public void setRouteName(String routeName) {
    this.routeName = routeName;
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
}
