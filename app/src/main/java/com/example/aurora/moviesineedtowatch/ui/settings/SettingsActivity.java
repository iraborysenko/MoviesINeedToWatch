package com.example.aurora.moviesineedtowatch.ui.settings;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.dagger.SharedPreferencesSettings;
import com.example.aurora.moviesineedtowatch.dagger.blocks.settingsscreen.DaggerSettingsScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.settingsscreen.SettingsScreenModule;
import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.tmdb.eventbus.EventsForChangeTheme;
import com.example.aurora.moviesineedtowatch.tools.Extensions;
import com.example.aurora.moviesineedtowatch.tools.LocaleHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.aurora.moviesineedtowatch.tools.Constants.SHARED_CURRENT_THEME;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 11/05/19
 * Time: 14:05
 */
public class SettingsActivity extends AppCompatActivity implements SettingsScreen.View {

    @Inject
    SharedPreferencesSettings sharedPreferencesSettings;

    @Inject
    SettingsPresenter mPresenter;

    private boolean userIsInteracting;

    @BindView(R.id.save_via_wifi) TextView mSaveViaWifi;
    @BindView(R.id.dark_theme) TextView mDarkTheme;
    @BindView(R.id.theme_switcher) Switch mDarkThemeSwitcher;
    @BindView(R.id.app_language) TextView mAppLanguage;
    @BindView(R.id.export_db_button) Button mExportDbButton;
    @BindView(R.id.import_db_button) Button mImportDbButton;
    @BindView(R.id.language_spinner) Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerSettingsScreenComponent.builder()
                .settingsScreenModule(new SettingsScreenModule(this))
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .build().inject(this);

        if(sharedPreferencesSettings.contains(SHARED_CURRENT_THEME))
            Extensions.setAppTheme(sharedPreferencesSettings.getBooleanData(SHARED_CURRENT_THEME), R.layout.activity_settings, this, this);
        else {
            sharedPreferencesSettings.putBooleanData(SHARED_CURRENT_THEME, false);
            Extensions.setAppTheme(false, R.layout.activity_settings, this, this);
        }

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.settings);

        mDarkThemeSwitcher.setChecked(sharedPreferencesSettings.getBooleanData(SHARED_CURRENT_THEME));
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

    @OnClick(R.id.export_db_button)
    void exportButton() {
        mPresenter.exportDb();
    }

    @OnClick(R.id.import_db_button)
    void importButton() {
        mPresenter.importDb(this);
    }

    @OnClick(R.id.theme_switcher)
    void saveChangedTheme() {
        if (mDarkThemeSwitcher.isChecked()) {
            sharedPreferencesSettings.putBooleanData(SHARED_CURRENT_THEME, true);
            EventBus.getDefault().post(new EventsForChangeTheme());
            recreate();
        } else {
            sharedPreferencesSettings.putBooleanData(SHARED_CURRENT_THEME, false);
            EventBus.getDefault().post(new EventsForChangeTheme());
            recreate();
        }
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
        mAppLanguage.setText(resources.getString(R.string.app_language));
        mExportDbButton.setText(resources.getString(R.string.export_button));
        mImportDbButton.setText(resources.getString(R.string.import_button));
    }
}
