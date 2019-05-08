package com.example.aurora.moviesineedtowatch.ui.main.towatchtab;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:17
 */
public class ToWatchFragmentPresenter implements ToWatchFragmentScreen.Presenter {

    private ToWatchFragmentScreen.View mView;

    @Inject
    ToWatchFragmentPresenter(ToWatchFragmentScreen.View mView) {
        this.mView = mView;
    }

}
