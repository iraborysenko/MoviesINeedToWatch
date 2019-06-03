package com.example.aurora.moviesineedtowatch.ui.main;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.aurora.moviesineedtowatch.App;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprers.TabsViewPagerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.SharedPreferencesSettings;
import com.example.aurora.moviesineedtowatch.tmdb.eventbus.EventsForChangeTheme;
import com.example.aurora.moviesineedtowatch.tmdb.eventbus.EventsForUpdateList;
import com.example.aurora.moviesineedtowatch.tools.Extensions;
import com.example.aurora.moviesineedtowatch.ui.main.towatchtab.ToWatchFragment;
import com.example.aurora.moviesineedtowatch.ui.main.watchedtab.WatchedFragment;
import com.example.aurora.moviesineedtowatch.ui.search.SearchActivity;
import com.example.aurora.moviesineedtowatch.tools.LocaleHelper;
import com.example.aurora.moviesineedtowatch.ui.settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.aurora.moviesineedtowatch.tools.Constants.INCREASED_LAYOUT;
import static com.example.aurora.moviesineedtowatch.tools.Constants.REDUCED_LAYOUT;
import static com.example.aurora.moviesineedtowatch.tools.Constants.SHARED_CURRENT_THEME;
import static com.example.aurora.moviesineedtowatch.tools.Constants.SHARED_TO_WATCH_LAYOUT;
import static com.example.aurora.moviesineedtowatch.tools.Constants.SHARED_WATCHED_LAYOUT;
import static com.example.aurora.moviesineedtowatch.tools.Constants.TO_WATCH_TAB;
import static com.example.aurora.moviesineedtowatch.tools.Constants.WATCHED_TAB;

public class MainActivity extends AppCompatActivity {

    @Inject
    SharedPreferencesSettings sharedPreferencesSettings;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private String previousLocale = "en";
    private String currentLayout;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplicationContext()).getApplicationComponent().inject(this);
        Extensions.setAppTheme(sharedPreferencesSettings.getBooleanData(SHARED_CURRENT_THEME), R.layout.activity_main, this, this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.movies_to_watch);

        EventBus.getDefault().register(this);

        ButterKnife.bind(this);

        setupViewPager(viewPager);

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_to_watch_tab:
                    viewPager.setCurrentItem(0);
                    updateLayoutItem(SHARED_TO_WATCH_LAYOUT);
                    break;
                case R.id.action_watched_tab:
                    viewPager.setCurrentItem(1);
                    updateLayoutItem(SHARED_WATCHED_LAYOUT);
                    break;
            }
            return true;
        });
    }

    public void setupViewPager(ViewPager viewPager) {
        TabsViewPagerAdapter tabsViewPagerAdapter = new TabsViewPagerAdapter(getSupportFragmentManager());
        tabsViewPagerAdapter.addFragment(new ToWatchFragment());
        tabsViewPagerAdapter.addFragment(new WatchedFragment());
        tabsViewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(tabsViewPagerAdapter);
    }

    /////=======Tab Menu Part
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_buttons, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_movie_layout_button:
                if (viewPager.getCurrentItem() == TO_WATCH_TAB)
                    currentLayout = SHARED_TO_WATCH_LAYOUT;
                else if (viewPager.getCurrentItem() == WATCHED_TAB)
                    currentLayout = SHARED_WATCHED_LAYOUT;

                switch (sharedPreferencesSettings.getStringData(currentLayout, INCREASED_LAYOUT)) {
                    case INCREASED_LAYOUT:
                        sharedPreferencesSettings.putStringData(currentLayout, REDUCED_LAYOUT);
                        item.setIcon(R.drawable.ic_increased_list);
                        break;
                    case REDUCED_LAYOUT:
                        sharedPreferencesSettings.putStringData(currentLayout, INCREASED_LAYOUT);
                        item.setIcon(R.drawable.ic_reduced_list);
                        break;
                    default:
                        break;
                }

                EventBus.getDefault().post(new EventsForUpdateList());
                return true;
            case R.id.search_button:
                startActivity(new Intent(this, SearchActivity.class));
                return true;

            case R.id.settings_button:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            default:
                Log.e("error", "No action");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLanguage();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventsForChangeTheme event) {
        recreate();
    }

    private void checkLanguage() {
        if (!LocaleHelper.getLanguage(MainActivity.this).equals(previousLocale)) {
            previousLocale = LocaleHelper.getLanguage(MainActivity.this);
            Resources resources = LocaleHelper.setLocale(MainActivity.this, previousLocale).getResources();
            updateUI(resources);
        }
    }

    private void updateUI(Resources resources) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(resources.getString(R.string.movies_to_watch));
        mBottomNavigationView.getMenu().findItem(R.id.action_to_watch_tab).setTitle(resources.getString(R.string.to_watch_tab));
        mBottomNavigationView.getMenu().findItem(R.id.action_watched_tab).setTitle(resources.getString(R.string.watched_tab));
    }

    private void updateLayoutItem(String currentLayout) {
        switch (sharedPreferencesSettings.getStringData(currentLayout, INCREASED_LAYOUT)) {
            case INCREASED_LAYOUT:
                menu.getItem(0).setIcon(R.drawable.ic_reduced_list);
                break;
            case REDUCED_LAYOUT:
                menu.getItem(0).setIcon(R.drawable.ic_increased_list);
                break;
            default:
                break;
        }
    }
}