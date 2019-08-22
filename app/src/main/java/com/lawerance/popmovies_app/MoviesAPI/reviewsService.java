package com.lawerance.popmovies_app.MoviesAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface reviewsService {
    @GET("reviews")
    Call<reviewsResponse> get_reviews(@Query("api_key") String token);
}
