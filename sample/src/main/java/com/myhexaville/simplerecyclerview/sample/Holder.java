package com.myhexaville.simplerecyclerview.sample;


import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.myhexaville.simplerecyclerview.sample.databinding.ListItemBinding;


public class Holder extends RecyclerView.ViewHolder {
    public ListItemBinding binding;

    public Holder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}
