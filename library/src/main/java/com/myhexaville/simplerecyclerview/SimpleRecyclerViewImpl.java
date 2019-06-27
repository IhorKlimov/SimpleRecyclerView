package com.myhexaville.simplerecyclerview;


import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.myhexaville.simplerecyclerview.listeners.EndlessScrollListener;
import com.myhexaville.simplerecyclerview.listeners.OnEmptyListener;

public class SimpleRecyclerViewImpl extends RecyclerView {
    private static final String LOG_TAG = "SimpleRecyclerView";
    private Runnable onLoadMoreListener;
    public EndlessScrollListener endlessListener;

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
        if (getAdapter() == null) {
            throw new NullPointerException("Call setDoneFetching() method only after you've set SimpleRecyclerView.Adapter");
        }
        endlessListener.setDoneFetching();
        Log.d(LOG_TAG, "setDoneFetching: " + endlessListener.sizeAfterFetching + " " + endlessListener.sizeBeforeFetching);
        if (endlessListener.sizeAfterFetching > endlessListener.sizeBeforeFetching) {
            getAdapter().notifyItemRangeInserted(
                    endlessListener.sizeBeforeFetching,
                    endlessListener.sizeAfterFetching - endlessListener.sizeBeforeFetching);
        } else if (endlessListener.sizeBeforeFetching == 0) {
            getAdapter().notifyDataSetChanged();
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
