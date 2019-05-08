package com.example.aurora.moviesineedtowatch.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprers.TabsViewPagerAdapter;
import com.example.aurora.moviesineedtowatch.ui.main.towatchtab.ToWatchFragment;
import com.example.aurora.moviesineedtowatch.ui.main.watchedtab.WatchedFragment;
import com.example.aurora.moviesineedtowatch.ui.search.SearchActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")

    @BindView(R.id.main_search_fab)
    FloatingActionButton mMainSearchFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ButterKnife.bind(this);

        mMainSearchFab.setOnClickListener(view -> searchTMDB());
    }

    public void setupViewPager(ViewPager viewPager) {
        TabsViewPagerAdapter tabsViewPagerAdapter = new TabsViewPagerAdapter(getSupportFragmentManager());
        tabsViewPagerAdapter.addFragment(new ToWatchFragment(), "To Watch");
        tabsViewPagerAdapter.addFragment(new WatchedFragment(), "Watched");
        tabsViewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(tabsViewPagerAdapter);
    }

    public void searchTMDB() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}