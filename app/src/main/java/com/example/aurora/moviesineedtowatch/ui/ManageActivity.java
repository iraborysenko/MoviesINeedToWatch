package com.example.aurora.moviesineedtowatch.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.aurora.moviesineedtowatch.R;

import java.util.Objects;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 02/03/18
 * Time: 18:41
 */
public class ManageActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_manage);
    }
}
