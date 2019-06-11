package com.example.aurora.moviesineedtowatch.ui.settings;

import android.app.Activity;

import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 10/06/19
 * Time: 20:36
 */
public class SettingsPresenter implements SettingsScreen.Presenter {
    private SettingsScreen.View mView;

    @Inject
    WishList wishList;

    @Inject
    SettingsPresenter(SettingsScreen.View mView) {
        this.mView = mView;
    }

    @Override
    public void exportDb() {
        wishList.exportDataBase();
    }

    @Override
    public void importDb(Activity activity) {
        wishList.importDataBase(activity);
    }
}
