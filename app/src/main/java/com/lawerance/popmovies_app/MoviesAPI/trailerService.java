package com.lawerance.popmovies_app.MoviesAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface  trailerService {
    @GET("videos")
    Call<trailersResponse> get_trailers(@Query("api_key") String token);

}
