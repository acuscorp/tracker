package com.acuscorp.googlemaps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class RouteViewModel extends ViewModel {

  private MutableLiveData<List<Route>> routes = new MutableLiveData<>() ;
  Route rout1 = new  Route(1,"Ruta roja",0.4,0.3);
  Route rout2 = new  Route(2,"Ruta verde",0.2,0.3);
  Route rout3 = new  Route(3,"Ruta amarilla",0.4,0.3);
  Route rout4 = new  Route(4,"Ruta azul",0.2,0.3);

  public RouteViewModel() {
    //instaciacion del repositorio
    List<Route> ListRoutes = new ArrayList<>();

    ListRoutes.add(rout1);
    ListRoutes.add(rout2);
    routes.setValue(ListRoutes);

  }

  public LiveData<List<Route>> getAllRoutes() {
    return routes;
  }

  public void setRoutesByUser(String username) {
    List<Route> routes = new ArrayList<>();
    routes.add(rout3);
    routes.add(rout4);
    this.routes.setValue(routes);
    //peticion de rutas por usuario
  }
}
