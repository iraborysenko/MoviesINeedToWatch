package com.example.aurora.moviesineedtowatch.ui.main.watchedtab;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.aurora.moviesineedtowatch.dagger.blocks.watchedfragment.DaggerWatchedFragmentScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.watchedfragment.WatchedFragmentScreenModule;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:17
 */
public class WatchedFragment extends Fragment implements WatchedFragmentScreen.View {

    @Inject
    WatchedFragmentPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerWatchedFragmentScreenComponent.builder()
                .watchedFragmentScreenModule(new WatchedFragmentScreenModule(this))
                .build().inject(this);
    }

}
