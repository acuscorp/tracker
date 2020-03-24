package com.acuscorp.googlemaps.routes;

import java.io.Serializable;

public class Route implements Serializable {
  private int id;
  private String routeName;
  private double Ilatitude;
  private double Ilongitude;
  private double Flatitude;
  private double Flongitude;

  public Route(int id, String routeName, double ilatitude, double ilongitude, double flatitude, double flongitude) {
    this.id = id;
    this.routeName = routeName;
    Ilatitude = ilatitude;
    Ilongitude = ilongitude;
    Flatitude = flatitude;
    Flongitude = flongitude;
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

  public double getIlatitude() {
    return Ilatitude;
  }

  public void setIlatitude(double ilatitude) {
    Ilatitude = ilatitude;
  }

  public double getIlongitude() {
    return Ilongitude;
  }

  public void setIlongitude(double ilongitude) {
    Ilongitude = ilongitude;
  }

  public double getFlatitude() {
    return Flatitude;
  }

  public void setFlatitude(double flatitude) {
    Flatitude = flatitude;
  }

  public double getFlongitude() {
    return Flongitude;
  }

  public void setFlongitude(double flongitude) {
    Flongitude = flongitude;
  }
}
