package com.lawerance.popmovies_app.MoviesAPI;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MoviesResponse {
    @SerializedName("results")

    private ArrayList<Result> mResults;

    public ArrayList<Result> getmResults() {
        return mResults;
    }

    public void setmResults(ArrayList<Result> mResults) {
        this.mResults = mResults;
    }
}
