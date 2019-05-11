package com.example.aurora.moviesineedtowatch.ui.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.aurora.moviesineedtowatch.R;

import java.util.Objects;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 11/05/19
 * Time: 14:05
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar settingsActionbar = getSupportActionBar();
        Objects.requireNonNull(settingsActionbar).setTitle(R.string.settings);
    }
}
