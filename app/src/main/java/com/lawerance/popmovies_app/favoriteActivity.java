package com.lawerance.popmovies_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.lawerance.popmovies_app.MoviesAPI.MoviesService;
import com.lawerance.popmovies_app.MoviesAPI.Result;
import com.lawerance.popmovies_app.database.FavMovieViewModel;
import com.lawerance.popmovies_app.database.MovieRoomDB;

import java.util.ArrayList;
import java.util.List;

public class favoriteActivity extends AppCompatActivity {
    private MovieAdapter adapter;
    private MovieRoomDB movieRoomDB;


    private RecyclerView mRecycleView;
    private ProgressBar progressBar;
    private GridLayoutManager gridLayoutManager;
    private MoviesService movieService;
    private ArrayList<Result> mMovies;
    private Intent intentMoreDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        mRecycleView = findViewById(R.id.main_recycler);
        movieRoomDB = MovieRoomDB.getInstance(this);
        adapter = new MovieAdapter(getApplicationContext());
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mRecycleView.setLayoutManager(gridLayoutManager);
        mRecycleView.setAdapter(adapter);
        setupViewModel();

        mRecycleView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecycleView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Result result = adapter.getItem(position);
                        intentMoreDetails=new Intent(getApplicationContext(),MoreDetails.class);
                        Log.d("+bara",result.getOriginalTitle());
                        intentMoreDetails.putExtra("result", result);
                        startActivity(intentMoreDetails);



                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

    }


    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
    private void setupViewModel() {
        FavMovieViewModel viewModel = ViewModelProviders.of(this).get(FavMovieViewModel.class);
        viewModel.getResultLiveData().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> movies) {
                for (int i = 0; i < movies.size(); i++) {
                    mMovies = new ArrayList<>();
                    adapter.add(movies.get(i));

                }            }
        });
    }


}
