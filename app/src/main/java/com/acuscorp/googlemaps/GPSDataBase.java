package com.acuscorp.googlemaps;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Noé Adrian Acuna Prado on 28/02/2020.
 *
 * acuscorp@gmail.com
 */
@Database(entities = {GPS.class},version = 1,exportSchema = false)
public abstract class GPSDataBase extends RoomDatabase {
    public static volatile GPSDataBase intance;
    public abstract GPSDAO gpsdao();
    private static final int NUMBER_OF_THREADS =4;


    static final ExecutorService databaseWriterExcecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized GPSDataBase getInstance(Context context){
        if(intance==null){
            intance = Room.databaseBuilder(context.getApplicationContext(),
                    GPSDataBase.class,"gps_database")
                    .build();
        }
        return intance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
    private static class PopulateDbAsynkTask extends AsyncTask<Void,Void,Void> {
        private GPSDAO gpsdao;
        private PopulateDbAsynkTask(GPSDataBase db){
            gpsdao = db.gpsdao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
