package com.lawerance.popmovies_app.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lawerance.popmovies_app.MoviesAPI.Result;

import java.util.List;

public class FavMovieViewModel extends AndroidViewModel {
    private LiveData<List<Result>> resultLiveData;
    public FavMovieViewModel(@NonNull Application application) {
        super(application);
    MovieRoomDB movieRoomDB=MovieRoomDB.getInstance(this.getApplication());
    resultLiveData=movieRoomDB.movieDao().loadMovies();
    }

    public LiveData<List<Result>> getResultLiveData(){

        return resultLiveData;
    }
}
