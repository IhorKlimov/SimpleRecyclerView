package com.myhexaville.simplerecyclerview.sample.databinding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.RESULT;

public class DataBindingAdapters {
    private static DiskCacheStrategy sCacheStrategy = RESULT;

    @BindingAdapter("app:imageUrl")
    public static void loadImage(ImageView v, String imgUrl) {

        Glide.with(v.getContext())
                .load(imgUrl)
                .diskCacheStrategy(sCacheStrategy)
                .into(v);
    }
}
