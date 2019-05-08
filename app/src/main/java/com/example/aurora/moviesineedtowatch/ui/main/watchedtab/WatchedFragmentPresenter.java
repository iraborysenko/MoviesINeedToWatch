package com.example.aurora.moviesineedtowatch.ui.main.watchedtab;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:18
 */
public class WatchedFragmentPresenter implements WatchedFragmentScreen.Presenter {

    private WatchedFragmentScreen.View mView;

    @Inject
    WatchedFragmentPresenter(WatchedFragmentScreen.View mView) {
        this.mView = mView;
    }

}
