package com.lawerance.popmovies_app;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public abstract class MovieScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;


    public MovieScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int ItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int ItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading()) {
            if ((ItemCount + ItemPosition) >= totalItemCount
                    && ItemPosition >= 0
                    ) {
                loadMoreMovies();

            }
        }

    }

    protected abstract void loadMoreMovies();


    public abstract boolean isLoading();

}
