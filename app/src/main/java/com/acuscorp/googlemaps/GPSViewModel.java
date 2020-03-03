package com.acuscorp.googlemaps;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class GPSViewModel extends AndroidViewModel {
    // TODO: Implement the
    private Repository repository;
    private  LiveData<List<GPS>> allGPSdata;


    public GPSViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allGPSdata = repository.getAllGPSdata();
    }

    public void insert(GPS gps){
        repository.insert(gps);

    }
    public void update(GPS gps){
        repository.update(gps);

    }
    public void delete(GPS gps){
        repository.delete(gps);

    }
    public void deletAllNotes(){
        repository.deleteAllGPSdata();
    }

    public LiveData<List<GPS>> getAllGPSdata() {
        return allGPSdata;
    }
}
