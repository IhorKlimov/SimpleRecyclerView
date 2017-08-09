package com.myhexaville.simplerecyclerview.listeners;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.myhexaville.simplerecyclerview.SimpleRecyclerView;


public class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private static final String LOG_TAG = "EndlessScrollListener";
    private boolean isLoading;
    public Runnable listener;

    public int sizeBeforeFetching, sizeAfterFetching;
    public SimpleRecyclerView.Adapter adapter;

    public EndlessScrollListener(Runnable listener) {
        this.listener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        Log.d(LOG_TAG, "onScrolled() called with:  dx = [" + dx + "], dy = [" + dy + "]");
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        adapter = (SimpleRecyclerView.Adapter) recyclerView.getAdapter();

        if (layoutManager.getChildCount() > 0) {
            int indexOfLastItemViewVisible = layoutManager.getChildCount() - 1;
            View lastItemViewVisible = layoutManager.getChildAt(indexOfLastItemViewVisible);
            int adapterPosition = layoutManager.getPosition(lastItemViewVisible);
            boolean isLastItemVisible = (adapterPosition >= adapter.getItemCount() - 3);


            if (isLastItemVisible && !isLoading) {
                fetchMore(adapter);
            }
        }
    }

    public void fetchMore(SimpleRecyclerView.Adapter adapter) {
        Log.d(LOG_TAG, "fetchMore: ");
        isLoading = true;
        if (this.adapter == null) {
            this.adapter = adapter;
        }
        sizeBeforeFetching = adapter.getCount();
        if (listener!=null  ) {
            Log.d(LOG_TAG, "fetchMore: run");
            listener.run();
        }
    }

    public void setDoneFetching() {
        isLoading = false;
        if (adapter != null) {
            sizeAfterFetching = adapter.getCount();
        }
    }

    public void reset() {
        sizeBeforeFetching = 0;
        sizeAfterFetching = 0;
    }
}