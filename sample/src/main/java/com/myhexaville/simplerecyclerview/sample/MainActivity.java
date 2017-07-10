package com.myhexaville.simplerecyclerview.sample;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import com.myhexaville.simplerecyclerview.sample.databinding.ActivityMainBinding;
import com.myhexaville.simplerecyclerview.sample.model.Movie;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    private Adapter adapter;
    private ActivityMainBinding binding;
    public ArrayList<Movie> list = new ArrayList<>();
    private boolean fetched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        setupList();
    }

    private void setupList() {
        list.add(new Movie("http://www.theonlinebeacon.com/wp-content/uploads/2016/01/star_wars_battlefront_dice.0.jpg"));
        list.add(new Movie("http://fantasynscifi.com/wp-content/uploads/2016/06/ogimage_img.jpg"));
        list.add(new Movie("https://secure.cdn2.wdpromedia.com/resize/mwImage/1/900/360/90/wdpromedia.disney.go.com/media/wdpro-hkdl-assets/prod/en-intl/system/images/hkdl-event-star-wars-hero-4character.jpg"));

        binding.list.setOnLoadMoreListener(() -> {
            new Handler().postDelayed(() -> {
                list.add(new Movie("http://www.theonlinebeacon.com/wp-content/uploads/2016/01/star_wars_battlefront_dice.0.jpg"));
                list.add(new Movie("http://fantasynscifi.com/wp-content/uploads/2016/06/ogimage_img.jpg"));
                list.add(new Movie("https://secure.cdn2.wdpromedia.com/resize/mwImage/1/900/360/90/wdpromedia.disney.go.com/media/wdpro-hkdl-assets/prod/en-intl/system/images/hkdl-event-star-wars-hero-4character.jpg"));

                binding.list.setDoneFetching();
            }, 3000);
        });
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(new Adapter(this, list));
    }
}
