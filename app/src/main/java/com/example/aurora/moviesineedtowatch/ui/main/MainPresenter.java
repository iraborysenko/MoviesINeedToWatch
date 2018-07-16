package com.example.aurora.moviesineedtowatch.ui.main;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/07/18
 * Time: 19:32
 */
public class MainPresenter implements MainScreen.Presenter{

    MainScreen.View mView;

    @Inject
    public MainPresenter(MainScreen.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadMovies(){
    }
}
