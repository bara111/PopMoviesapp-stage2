package com.lawerance.popmovies_app.MoviesAPI;


import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class reviewsResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private ArrayList<comment> results = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public reviewsResponse withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public reviewsResponse withPage(Integer page) {
        this.page = page;
        return this;
    }

    public ArrayList<comment> getResults() {
        return results;
    }

    public void setResults(ArrayList<comment> results) {
        this.results = results;
    }

    public reviewsResponse withResults(ArrayList<comment> results) {
        this.results = results;
        return this;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public reviewsResponse withTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public reviewsResponse withTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
        return this;
    }

}