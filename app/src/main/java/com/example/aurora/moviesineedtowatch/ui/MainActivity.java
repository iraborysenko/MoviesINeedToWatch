package com.example.aurora.moviesineedtowatch.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aurora.moviesineedtowatch.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
//        movieTMDB();
        searchTMDB();
    }

    public void movieTMDB() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }

    public void searchTMDB() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}