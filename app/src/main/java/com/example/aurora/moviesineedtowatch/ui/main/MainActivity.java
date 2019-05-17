package com.example.aurora.moviesineedtowatch.ui.main;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprers.TabsViewPagerAdapter;
import com.example.aurora.moviesineedtowatch.ui.main.towatchtab.ToWatchFragment;
import com.example.aurora.moviesineedtowatch.ui.main.watchedtab.WatchedFragment;
import com.example.aurora.moviesineedtowatch.ui.search.SearchActivity;
import com.example.aurora.moviesineedtowatch.tools.LocaleHelper;
import com.example.aurora.moviesineedtowatch.ui.settings.SettingsActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private String previousLocale = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.movies_to_watch);

        ButterKnife.bind(this);

        setupViewPager(viewPager);

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_to_watch_tab:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.action_watched_tab:
                    viewPager.setCurrentItem(1);
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_movie_layout_button:
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

    /////=======Language Part
    @Override
    protected void onResume() {
        super.onResume();
        checkLanguage();
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
}