package com.lawerance.popmovies_app.MoviesAPI;


import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class trailersResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private ArrayList<videos> videos = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public trailersResponse withId(Integer id) {
        this.id = id;
        return this;
    }

    public ArrayList<videos> getResults() {
        return videos;
    }

    public void setResults(ArrayList<videos> results) {
        this.videos = results;
    }

    public trailersResponse withResults(ArrayList<videos> results) {
        this.videos = results;
        return this;
    }

}