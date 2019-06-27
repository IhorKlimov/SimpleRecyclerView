package com.myhexaville.simplerecyclerview;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.myhexaville.simplerecyclerview.holders.ProgressBarHolder;
import com.myhexaville.simplerecyclerview.listeners.OnEmptyListener;

public class SimpleRecyclerView extends FrameLayout implements OnEmptyListener {
    private static final int[] NESTED_SCROLLING_ATTRS
            = {16843830 /* android.R.attr.nestedScrollingEnabled */};
    private static final String LOG_TAG = "SimpleRecyclerView";
    private SimpleRecyclerViewImpl recycler;
    private ProgressBar progressBar;
    private ViewStub emptyView;
    private int progressLayout;
    private boolean footerEnabled;
    private boolean fetchedInitial;
    private Adapter adapter;

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
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SimpleRecyclerView,
                0, 0);

        TypedArray at = context.obtainStyledAttributes(attrs, NESTED_SCROLLING_ATTRS, -1, -1);
        boolean nestedScrollingEnabled = a.getBoolean(0, true);
        recycler.setNestedScrollingEnabled(nestedScrollingEnabled);
        at.recycle();

        try {

            int emptyLayout = a.getResourceId(R.styleable.SimpleRecyclerView_empty_layout, -1);
            progressLayout = a.getResourceId(R.styleable.SimpleRecyclerView_progress_layout, -1);
            boolean initialProgressLayoutEnabled = a.getBoolean(R.styleable.SimpleRecyclerView_initial_progress_bar_enabled, true);

            if (!initialProgressLayoutEnabled) {
                progressBar.setVisibility(GONE);
            }

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
        this.adapter = adapter;
        this.adapter.progressLayout = progressLayout;
        adapter.isFooterEnabled = footerEnabled;
    }

    public void setOnLoadMoreListener(Runnable listener) {
        recycler.setOnLoadMoreListener(listener);
        this.footerEnabled = true;
        adapter.isFooterEnabled = true;
    }

    public void addItemDecoration(@NonNull  RecyclerView.ItemDecoration decor) {
        recycler.addItemDecoration(decor);
    }

    public void addItemDecoration(@NonNull  RecyclerView.ItemDecoration decor, int index) {
        recycler.addItemDecoration(decor, index);
    }

    public void setDoneFetching() {
        if (!fetchedInitial) {
            fetchedInitial = true;
        }
        if (progressBar.getVisibility() == VISIBLE) {
            progressBar.setVisibility(GONE);
        }
        recycler.setDoneFetching();
    }

    public void setNoMoreToFetch() {
        recycler.setNoMoreToFetch();
    }

    @Override
    public void onEmptyList() {
        if (emptyView != null && fetchedInitial) {
            emptyView.setVisibility(VISIBLE);
        }
    }

    public void setInsideNestedScrollView(NestedScrollView nestedScroll) {
        recycler.clearOnScrollListeners();

        nestedScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    recycler.endlessListener.fetchMore(adapter);
                }
            }
        });
    }

    public static abstract class Adapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @LayoutRes
        int progressLayout;
        OnEmptyListener setOnEmptyListener;
        boolean isFooterEnabled;
        private static final int ITEM_TYPE_BODY = 1;
        private static final int ITEM_TYPE_FOOTER = 2;

        @Override
        final public int getItemViewType(int position) {
            if (isFooterEnabled) {
                Log.d(LOG_TAG, "getItemViewType: footer enabled");
                if (position == getItemCount() - 1) {
                    Log.d(LOG_TAG, "getItemViewType: footer 1");
                    return ITEM_TYPE_FOOTER;
                } else {
                    Log.d(LOG_TAG, "getItemViewType: footer 2");
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
        final public int getItemCount() {
            Log.d(LOG_TAG, "getItemCount: " + getCount());
            if (getCount() == 0) {
                setOnEmptyListener.onEmptyList();
                return 0;
            } else {
                return isFooterEnabled ? getCount() + 1 : getCount();
            }
        }

        @Override
        final public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
