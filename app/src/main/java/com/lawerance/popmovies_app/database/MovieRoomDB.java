package com.lawerance.popmovies_app.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.lawerance.popmovies_app.MoviesAPI.Result;

@Database(entities ={Result.class},version = 1,exportSchema = false)
public abstract class MovieRoomDB extends RoomDatabase {
    private static final String DATABASE_NAME="FavoriteMovies";
    private static final Object LOCK=new Object();
    private static MovieRoomDB sInstance;

    public static MovieRoomDB getInstance(Context context){
        if(sInstance==null){
            synchronized (LOCK){
                sInstance= Room.databaseBuilder(context.getApplicationContext(), MovieRoomDB.class, MovieRoomDB.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
