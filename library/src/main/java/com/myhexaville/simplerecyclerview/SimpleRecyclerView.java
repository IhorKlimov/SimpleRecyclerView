package com.myhexaville.simplerecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.myhexaville.simplerecyclerview.holders.ProgressBarHolder;
import com.myhexaville.simplerecyclerview.listeners.OnEmptyListener;

public class SimpleRecyclerView extends FrameLayout implements OnEmptyListener {
    private static final String LOG_TAG = "SimpleRecyclerView";
    private SimpleRecyclerViewImpl recycler;
    private ViewStub emptyView;
    private int progressLayout;
    private boolean footerEnabled;

    public SimpleRecyclerView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public SimpleRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SimpleRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attrs) {
        inflate(context, R.layout.simple_recycler_view, this);
        recycler = (SimpleRecyclerViewImpl) findViewById(R.id.recycler);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SimpleRecyclerView,
                0, 0);

        try {
            int emptyLayout = a.getResourceId(R.styleable.SimpleRecyclerView_empty_layout, -1);
            progressLayout = a.getResourceId(R.styleable.SimpleRecyclerView_progress_layout, -1);

            if (emptyLayout != -1) {
                emptyView = (ViewStub) findViewById(R.id.empty_view);
                emptyView.setLayoutResource(emptyLayout);
                emptyView.inflate();
                emptyView.setVisibility(INVISIBLE);
            }

            if (progressLayout == -1) {
                progressLayout = R.layout.progress_list_item;
            }
        } finally {
            a.recycle();
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recycler.setLayoutManager(layoutManager);
    }

    public void setAdapter(Adapter adapter) {
        recycler.setAdapter(adapter);
        recycler.setOnEmptyListener(this);
        adapter.progressLayout = progressLayout;
        adapter.isFooterEnabled = footerEnabled;
    }

    public void setOnLoadMoreListener(Runnable listener) {
        recycler.setOnLoadMoreListener(listener);
        this.footerEnabled = true;
    }

    public void setDoneFetching() {
        recycler.setDoneFetching();
    }

    public void setNoMoreToFetch() {
        recycler.setNoMoreToFetch();
    }

    @Override
    public void onEmptyList() {
        if (emptyView!=null) {
            emptyView.setVisibility(VISIBLE);
        }
    }

    public static abstract class Adapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @LayoutRes int progressLayout;
        OnEmptyListener setOnEmptyListener;
         boolean isFooterEnabled;
        private static final int ITEM_TYPE_BODY = 1;
        private static final int ITEM_TYPE_FOOTER = 2;

        @Override
        final public int getItemViewType(int position) {
            if (isFooterEnabled) {
                if (position == getItemCount() - 1) {
                    return ITEM_TYPE_FOOTER;
                } else {
                    return ITEM_TYPE_BODY;
                }
            } else {
                return ITEM_TYPE_BODY;
            }
        }

        @Override
        final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM_TYPE_FOOTER) {
                View inflate = LayoutInflater.from(parent.getContext())
                        .inflate(progressLayout, parent, false);
                return new ProgressBarHolder(inflate);
            } else {
                return onCreateHolder(parent);
            }
        }

        @Override
        final  public int getItemCount() {
            if (getCount() == 0) {
                setOnEmptyListener.onEmptyList();
                return 0;
            } else {
                return isFooterEnabled ? getCount() + 1 : getCount();
            }
        }

        @Override
        final  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (isFooterEnabled && position == getItemCount() - 1) {

            } else {
                onBindHolder((VH) holder, position);
            }
        }

        public abstract VH onCreateHolder(ViewGroup parent);

        public abstract int getCount();

        public abstract void onBindHolder(VH holder, int position);
    }

}
