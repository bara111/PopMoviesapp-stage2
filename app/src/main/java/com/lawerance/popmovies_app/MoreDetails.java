package com.lawerance.popmovies_app;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

//import com.lawerance.popmovies_app.Database_Room.MovieViewModel;
import com.lawerance.popmovies_app.MoviesAPI.Result;
import com.lawerance.popmovies_app.MoviesAPI.comment;
import com.lawerance.popmovies_app.MoviesAPI.reviewsResponse;
import com.lawerance.popmovies_app.MoviesAPI.reviewsService;
import com.lawerance.popmovies_app.MoviesAPI.trailerService;
import com.lawerance.popmovies_app.MoviesAPI.trailersResponse;
import com.lawerance.popmovies_app.MoviesAPI.videos;
import com.lawerance.popmovies_app.database.AppExecutors;
import com.lawerance.popmovies_app.database.MovieRoomDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MoreDetails extends AppCompatActivity {
    private ImageView mPosterimageView, mBackdropimageView;
    private TextView mTitle, mOriginalLanguage, mReleaseDate, mVoteAverage, mOverview, trailerText;
    private final String url = "https://www.youtube.com/watch?v=";
    private ArrayList<videos> trailers;
    private ArrayList<comment> comments;
    private RecyclerView recyclerViewComments;
    private LinearLayoutManager linearLayoutManagerReviews;
    private comment comment;
    private reviewsAdapter reviewsAdapter;

    private RecyclerView recyclerViewTrailers;
    private LinearLayoutManager linearLayoutManagerTrailers;
    private Result result;

    private MenuItem mItem;
    private MovieRoomDB movieRoomDB;
    private boolean MoviedFounded;
    private static final String MOVIE_API_KEY = "9e83a1425b48215d005e8817dc0d95ee";
    private TrailerAdapter trailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        recyclerViewComments=findViewById(R.id.Reviews_recycler);
        trailerText = findViewById(R.id.Trailers);
        recyclerViewTrailers = findViewById(R.id.Trailers_recycler);
        movieRoomDB = MovieRoomDB.getInstance(this);
        Intent intent = getIntent();
        result = intent.getParcelableExtra("result");
        String Title = result.getOriginalTitle();
        String OriginalLanguage = result.getOriginalLanguage();
        String ReleaseDate = result.getReleaseDate();
        String BackdropPath = result.getBackdropPath();
        String VoteAverage = result.getVoteAverage();
        String PosterPath = result.getPosterPath();
        String Overview = result.getOverview();

        mOverview = findViewById(R.id.tv_overview);
        mPosterimageView = findViewById(R.id.iv_poster_image);
        mBackdropimageView = findViewById(R.id.iv_background_image);
        mTitle = findViewById(R.id.tv_title);
        mOriginalLanguage = findViewById(R.id.tv_language);
        mReleaseDate = findViewById(R.id.tv_date);
        mVoteAverage = findViewById(R.id.tv_rating);
        mTitle.setText(Title);
        mOverview.setText(Overview);
        mOriginalLanguage.setText(OriginalLanguage);
        mReleaseDate.setText(ReleaseDate);
        mVoteAverage.setText(VoteAverage);

        Glide
                .with(this)
                .load("https://image.tmdb.org/t/p/original" + PosterPath)
                .into(mPosterimageView);
        Glide
                .with(this)
                .load("https://image.tmdb.org/t/p/original" + BackdropPath)
                .into(mBackdropimageView);


        Retrofit retrofitTrailers = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/" + result.getId() + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        trailerService trailersService = retrofitTrailers.create(trailerService.class);
        trailersService.get_trailers(MOVIE_API_KEY).enqueue(new Callback<trailersResponse>() {
            @Override
            public void onResponse(Call<trailersResponse> call, Response<trailersResponse> response) {
                trailers = response.body().getResults();

                linearLayoutManagerTrailers = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
                recyclerViewTrailers.setLayoutManager(linearLayoutManagerTrailers);
                trailerAdapter = new TrailerAdapter(getApplicationContext());
                recyclerViewTrailers.setAdapter(trailerAdapter);
                trailerAdapter.addAll(trailers);


            }

            @Override
            public void onFailure(Call<trailersResponse> call, Throwable t) {

            }
        });
        Retrofit retrofitReviews = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/" + result.getId() + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        reviewsService reviewsService = retrofitReviews.create(reviewsService.class);
        reviewsService.get_reviews(MOVIE_API_KEY).enqueue(new Callback<reviewsResponse>() {
            @Override
            public void onResponse(Call<reviewsResponse> call, Response<reviewsResponse> response) {
                comments = response.body().getResults();

                linearLayoutManagerReviews = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                recyclerViewComments.setLayoutManager(linearLayoutManagerReviews);
                reviewsAdapter = new reviewsAdapter(getApplicationContext());
                recyclerViewComments.setAdapter(reviewsAdapter);
                reviewsAdapter.addAll(comments);

            }

            @Override
            public void onFailure(Call<reviewsResponse> call, Throwable t) {

            }
        });


        recyclerViewTrailers.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerViewTrailers, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        videos result = trailerAdapter.getItem(position);

                        String VideoUrl = url + result.getKey();

                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(VideoUrl));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setPackage("com.android.chrome");
                        startActivity(i);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_more_details, menu);
        LiveData<List<Result>> movies = movieRoomDB.movieDao().loadMovies();
        movies.observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> movies) {
                for (int i = 0; i < movies.size(); i++) {
                    if (movies.get(i).getId() == result.getId()) {
                        menu.findItem(R.id.favorite).setIcon(R.drawable.liked);
                        MoviedFounded = true;
                        break;
                    } else {
                        menu.findItem(R.id.favorite).setIcon(R.drawable.like);
                        MoviedFounded = false;
                    }
                }

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mItem = item;
        switch (item.getItemId()) {
            case R.id.favorite:


                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (MoviedFounded) {
                            movieRoomDB.movieDao().deleteTask(result);

                        } else {

                            movieRoomDB.movieDao().insertTask(result);


                        }
                    }
                });
                if (MoviedFounded) {
                    mItem.setIcon(R.drawable.like);

                } else {
                    mItem.setIcon(R.drawable.liked);

                }

                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}
