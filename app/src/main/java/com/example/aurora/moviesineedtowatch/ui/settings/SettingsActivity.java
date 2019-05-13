package com.example.aurora.moviesineedtowatch.ui.settings;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tools.LocaleHelper;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 11/05/19
 * Time: 14:05
 */
public class SettingsActivity extends AppCompatActivity {

    private boolean userIsInteracting;

    @BindView(R.id.save_via_wifi) TextView mSaveViaWifi;
    @BindView(R.id.dark_theme) TextView mDarkTheme;
    @BindView(R.id.base_list_layout) TextView mBaseListLayout;
    @BindView(R.id.app_language) TextView mAppLanguage;
    @BindView(R.id.export_movies) TextView mExportMovies;
    @BindView(R.id.language_spinner) Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.settings);

        ButterKnife.bind(this);

        switch (LocaleHelper.getLanguage(SettingsActivity.this)) {
            case "ru": mSpinner.setSelection(1);
                break;
            case "uk": mSpinner.setSelection(2);
                break;
            default: mSpinner.setSelection(0);
                break;
        }

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (userIsInteracting) {
                    if (position == 0) {
                        updateUI(LocaleHelper.setLocale(SettingsActivity.this, "en"));
                    } else if (position == 1) {
                        updateUI(LocaleHelper.setLocale(SettingsActivity.this, "ru"));
                    } else if (position == 2) {
                        updateUI(LocaleHelper.setLocale(SettingsActivity.this, "uk"));
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    private void updateUI(Context context) {
        Resources resources = context.getResources();
        Objects.requireNonNull(getSupportActionBar()).setTitle(resources.getString(R.string.settings));
        mSaveViaWifi.setText(resources.getString(R.string.save_via_wifi));
        mDarkTheme.setText(resources.getString(R.string.apply_dark_theme));
        mBaseListLayout.setText(resources.getString(R.string.base_movies_layout));
        mAppLanguage.setText(resources.getString(R.string.app_language));
        mExportMovies.setText(resources.getString(R.string.export_my_movies_list));
    }
}
