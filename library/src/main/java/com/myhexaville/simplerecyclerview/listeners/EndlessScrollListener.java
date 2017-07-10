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
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        adapter = (SimpleRecyclerView.Adapter) recyclerView.getAdapter();

        if (layoutManager.getChildCount() > 0) {
            int indexOfLastItemViewVisible = layoutManager.getChildCount() - 1;
            View lastItemViewVisible = layoutManager.getChildAt(indexOfLastItemViewVisible);
            int adapterPosition = layoutManager.getPosition(lastItemViewVisible);
            boolean isLastItemVisible = (adapterPosition >= adapter.getItemCount() - 3);


            if (isLastItemVisible && !isLoading) {
                Log.d(LOG_TAG, "onScrolled: ");
                isLoading = true;
                sizeBeforeFetching = adapter.getCount();
                if (listener!=null  ) {
                    listener.run();
                }
            }
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