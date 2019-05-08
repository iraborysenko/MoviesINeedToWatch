package com.example.aurora.moviesineedtowatch.ui.main.watchedtab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.watchedfragment.DaggerWatchedFragmentScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.watchedfragment.WatchedFragmentScreenModule;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watched, container, false);

        return view;
    }
}
