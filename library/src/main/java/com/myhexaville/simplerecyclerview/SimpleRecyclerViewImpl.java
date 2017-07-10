package com.myhexaville.simplerecyclerview;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myhexaville.simplerecyclerview.holders.ProgressBarHolder;
import com.myhexaville.simplerecyclerview.listeners.EndlessScrollListener;
import com.myhexaville.simplerecyclerview.listeners.OnEmptyListener;

public class SimpleRecyclerViewImpl extends RecyclerView {
    private static final String LOG_TAG = "SimpleRecyclerView";
    private Runnable onLoadMoreListener;
    private EndlessScrollListener endlessListener;

    public SimpleRecyclerViewImpl(Context context) {
        super(context);
        init();
    }

    public SimpleRecyclerViewImpl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleRecyclerViewImpl(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        endlessListener = new EndlessScrollListener(onLoadMoreListener);
        addOnScrollListener(endlessListener);
    }

    public void setOnLoadMoreListener(Runnable onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        endlessListener.listener = onLoadMoreListener;
    }

    public void setDoneFetching() {
        endlessListener.setDoneFetching();
        if (endlessListener.sizeAfterFetching > endlessListener.sizeBeforeFetching) {
            getAdapter().notifyItemRangeInserted(
                    endlessListener.sizeBeforeFetching,
                    endlessListener.sizeAfterFetching - endlessListener.sizeBeforeFetching);
        }
    }

    public void setNoMoreToFetch() {
        ((SimpleRecyclerView.Adapter) getAdapter()).isFooterEnabled = false;
        getAdapter().notifyItemRemoved(((SimpleRecyclerView.Adapter) getAdapter()).getCount());
    }

    public void setOnEmptyListener(OnEmptyListener onEmptyListener) {
        ((SimpleRecyclerView.Adapter) getAdapter()).setOnEmptyListener = onEmptyListener;
    }

}
