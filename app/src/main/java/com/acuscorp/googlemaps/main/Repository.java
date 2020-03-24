package com.acuscorp.googlemaps.main;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.acuscorp.googlemaps.main.database.GPS;
import com.acuscorp.googlemaps.main.database.GPSDAO;
import com.acuscorp.googlemaps.main.database.GPSDataBase;

import java.util.List;

/**
 * Created by Noé Adrian Acuna Prado on 28/02/2020.
 *
 * acuscorp@gmail.com
 */
public class Repository {
  private GPSDAO gpsdao;
  private LiveData<List<GPS>> allGPSdata;

    public Repository(Application application) {
        GPSDataBase dataBase = GPSDataBase.getInstance(application);
        gpsdao = dataBase.gpsdao();
        allGPSdata = gpsdao.getAllGPSData();
    }

    public  void insert(GPS gps){
        new InsertGPSAsynkTask(gpsdao).execute(gps);
    }
    public void update(GPS gps){
        new UpdateGPSAsynkTask(gpsdao).equals(gps);
    }

    public  void delete(GPS gps){
        new DeleteGPSAsynkTask(gpsdao).execute(gps);
    }
    public  void deleteAllGPSdata(){
        new DeleteAllNoteAsynkTask(gpsdao).execute();
    }

    public LiveData<List<GPS>> getAllGPSdata() {
        return allGPSdata;
    }

    public static class InsertGPSAsynkTask extends AsyncTask<GPS,Void,Void> {
        private GPSDAO gpsdao;

        public InsertGPSAsynkTask(GPSDAO gpsdao) {
            this.gpsdao = gpsdao;
        }

        @Override
        protected Void doInBackground(GPS... gps) {
            gpsdao.insert(gps[0]);
            return null;
        }
    }
    public static class UpdateGPSAsynkTask extends AsyncTask<GPS,Void,Void>{
        private GPSDAO noteDao;

        public UpdateGPSAsynkTask(GPSDAO gpsdao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(GPS... gps) {
            noteDao.update(gps[0]);
            return null;
        }
    }
    public static class DeleteGPSAsynkTask extends AsyncTask<GPS,Void,Void>{
        private GPSDAO gpsdao;

        public DeleteGPSAsynkTask(GPSDAO gpsdao) {
            this.gpsdao = gpsdao;
        }

        @Override
        protected Void doInBackground(GPS... gps) {
            gpsdao.delete(gps[0]);
            return null;
        }
    }
    public static class DeleteAllNoteAsynkTask extends AsyncTask<Void,Void,Void>{
        private GPSDAO gpsdao;

        public DeleteAllNoteAsynkTask(GPSDAO gpsdao) {
            this.gpsdao = gpsdao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            gpsdao.deleteAllGPSData( );
            return null;
        }
    }
}
