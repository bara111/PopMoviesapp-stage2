package com.lawerance.popmovies_app.MoviesAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesService {
    @GET("/3/movie/top_rated")
    Call<MoviesResponse> get_top_rated(@Query("api_key") String token, @Query("page") String page);
    @GET("/3/movie/popular")
    Call<MoviesResponse> get_popular(@Query("api_key") String token, @Query("page") String page);

}
