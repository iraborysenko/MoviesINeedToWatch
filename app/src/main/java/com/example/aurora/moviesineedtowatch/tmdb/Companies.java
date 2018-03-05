package com.example.aurora.moviesineedtowatch.tmdb;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 05/03/18
 * Time: 22:52
 */
public class Companies {
    @SerializedName("id")
    private String name;

    public Companies() {

    }

    public String getName() {
        return name;
    }
}
