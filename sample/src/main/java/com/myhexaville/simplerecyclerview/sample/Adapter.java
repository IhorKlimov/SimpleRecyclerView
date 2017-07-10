package com.myhexaville.simplerecyclerview.sample;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.myhexaville.simplerecyclerview.SimpleRecyclerView;
import com.myhexaville.simplerecyclerview.sample.model.Movie;

import java.util.List;

public class Adapter extends SimpleRecyclerView.Adapter<Holder> {
    public List<Movie> list;
    private Context context;

    public Adapter(Context context, List<Movie> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateHolder(ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.list_item, parent, false);
        return new Holder(binding.getRoot());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void onBindHolder(Holder holder, int position) {
        Movie m = list.get(position);
        holder.binding.setMovie(m);
        holder.binding.text.setText(position+"");
    }

}
