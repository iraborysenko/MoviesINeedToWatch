package com.example.aurora.moviesineedtowatch.ui.main.towatchtab;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.aurora.moviesineedtowatch.dagger.blocks.towatchfragment.DaggerToWatchFragmentScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.towatchfragment.ToWatchFragmentScreenModule;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:16
 */
public class ToWatchFragment extends Fragment implements ToWatchFragmentScreen.View {

    @Inject
    ToWatchFragmentPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerToWatchFragmentScreenComponent.builder()
                .toWatchFragmentScreenModule(new ToWatchFragmentScreenModule(this))
                .build().inject(this);
    }
}
