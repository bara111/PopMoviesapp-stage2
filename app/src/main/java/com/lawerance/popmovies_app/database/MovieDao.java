package com.lawerance.popmovies_app.database;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.lawerance.popmovies_app.MoviesAPI.Result;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Result")
    LiveData<List<Result>> loadMovies();
    @Insert
    void insertTask(Result taskEntry);

    @Delete
    void deleteTask(Result taskEntry);

}
