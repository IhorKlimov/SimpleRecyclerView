package com.myhexaville.simplerecyclerview.sample.model;


public class Movie {
    private String posterUrl;

    public Movie(String mMovieUrl) {
        this.posterUrl = mMovieUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }
}