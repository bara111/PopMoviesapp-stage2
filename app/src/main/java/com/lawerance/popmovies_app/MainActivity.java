package com.lawerance.popmovies_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lawerance.popmovies_app.MoviesAPI.MoviesResponse;
import com.lawerance.popmovies_app.MoviesAPI.MoviesService;
import com.lawerance.popmovies_app.MoviesAPI.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String SCROLL_POSITION = "SCROLL_POSITION";

    private static final String MOVIES = "SAVE_MOVIES";
    private static final String PAGE_NUMBER = "PAGE_NUMBER";
    private static final String MOVIE_SORTING = "MOVIE_SORTING";
    private static final String MOVIE_API_KEY = "9e83a1425b48215d005e8817dc0d95ee";
    private static final int PAGE_START = 1;

    private MovieAdapter adapter;


    private RecyclerView mRecycleView;
    private ProgressBar progressBar;


    private boolean popular = false;
    private boolean isLoading = false;
    private int Position;
    private int mCurrentPage = PAGE_START;
    private GridLayoutManager gridLayoutManager;
    private MoviesService movieService;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);
        intent = new Intent(MainActivity.this, MoreDetails.class);

        mRecycleView = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        adapter = new MovieAdapter(this);

        gridLayoutManager = new GridLayoutManager(this, 3);
        mRecycleView.setLayoutManager(gridLayoutManager);

        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        mRecycleView.setAdapter(adapter);

        mRecycleView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecycleView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Result result = adapter.getItem(position);
                        intent.putExtra("result", result);

                        startActivity(intent);


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        mRecycleView.addOnScrollListener(new MovieScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreMovies() {
                isLoading = true;
                mCurrentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage_TopRated();
                    }
                }, 1000);
            }



            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        movieService = retrofit.create(MoviesService.class);
        if (savedInstanceState == null) {
            loadFirstPage_TopRated();
        } else {

            progressBar.setVisibility(View.GONE);
            popular = savedInstanceState.getBoolean(MOVIE_SORTING);
            mCurrentPage = savedInstanceState.getInt(PAGE_NUMBER);
            ArrayList<Result> movies = savedInstanceState.getParcelableArrayList(MOVIES);
            if (popular) {
                mRecycleView.addOnScrollListener(new MovieScrollListener(gridLayoutManager) {
                    @Override
                    protected void loadMoreMovies() {
                        isLoading = true;
                        mCurrentPage += 1;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadNextPage_popular();
                            }
                        }, 1000);
                    }

                    @Override
                    public boolean isLoading() {
                        return isLoading;
                    }
                });

            }
            adapter.addAll(movies);
            ((GridLayoutManager) mRecycleView.getLayoutManager()).scrollToPositionWithOffset(Position,0);

        }


    }


    private void loadFirstPage_TopRated() {

        movieService.get_top_rated(
                MOVIE_API_KEY,
                mCurrentPage + ""
        ).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                ArrayList<Result> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);

                adapter.addAll(results);
                adapter.addLoadingFooter();

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });

    }


    private void loadFirstPage_popular() {

        movieService.get_popular(
                MOVIE_API_KEY,
                mCurrentPage + ""
        ).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                ArrayList<Result> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);
                adapter.addLoadingFooter();

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });

    }


    private ArrayList<Result> fetchResults(Response<MoviesResponse> response) {
        MoviesResponse topRatedMovies = response.body();
        return topRatedMovies.getmResults();
    }

    private void loadNextPage_TopRated() {

        movieService.get_top_rated(
                MOVIE_API_KEY,
                mCurrentPage + ""
        ).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                ArrayList<Result> results = fetchResults(response);
                adapter.addAll(results);

                adapter.addLoadingFooter();

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });
    }

    private void loadNextPage_popular() {
        Log.d(TAG, "loadNextPage: " + mCurrentPage);

        movieService.get_popular(
                MOVIE_API_KEY,
                mCurrentPage + ""
        ).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                ArrayList<Result> results = fetchResults(response);
                adapter.addAll(results);

                adapter.addLoadingFooter();

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t){
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_ratted:
                mCurrentPage = 1;
                popular = false;
                adapter = new MovieAdapter(this);
                gridLayoutManager = new GridLayoutManager(this, 3);
                mRecycleView.setLayoutManager(gridLayoutManager);

                mRecycleView.setItemAnimator(new DefaultItemAnimator());

                mRecycleView.setAdapter(adapter);
                loadFirstPage_TopRated();
                mRecycleView.addOnScrollListener(new MovieScrollListener(gridLayoutManager) {
                    @Override
                    protected void loadMoreMovies() {
                        isLoading = true;
                        mCurrentPage += 1;

                        // mocking network delay for API call
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadNextPage_TopRated();
                            }
                        }, 1000);
                    }





                    @Override
                    public boolean isLoading() {
                        return isLoading;
                    }
                });
                return true;
            case R.id.popular:
                mCurrentPage = 1;
                popular = true;
                adapter = new MovieAdapter(this);
                gridLayoutManager = new GridLayoutManager(this, 3);
                mRecycleView.setLayoutManager(gridLayoutManager);

                mRecycleView.setItemAnimator(new DefaultItemAnimator());

                mRecycleView.setAdapter(adapter);
                loadFirstPage_popular();
                mRecycleView.addOnScrollListener(new MovieScrollListener(gridLayoutManager) {
                    @Override
                    protected void loadMoreMovies() {
                        isLoading = true;
                        mCurrentPage += 1;

                        // mocking network delay for API call
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadNextPage_popular();
                            }
                        }, 1000);
                    }

                    @Override
                    public boolean isLoading() {
                        return isLoading;
                    }
                });
                return true;
            case R.id.favorite:
                Intent intent = new Intent(this, favoriteActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES, adapter.getMovies());
        outState.putInt(PAGE_NUMBER, mCurrentPage);
        outState.putBoolean(MOVIE_SORTING, popular);
        Position = ((GridLayoutManager)mRecycleView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putInt(SCROLL_POSITION,Position);
    }
}