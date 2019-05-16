package com.example.aurora.moviesineedtowatch.dagger.blocks.managescreen;

import com.example.aurora.moviesineedtowatch.dagger.module.WishListModule;
import com.example.aurora.moviesineedtowatch.ui.manage.ManageScreen;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 19/07/18
 * Time: 14:55
 */
@Module(includes = WishListModule.class)
public class ManageScreenModule {
    private final ManageScreen.View mView;

    public ManageScreenModule(ManageScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    ManageScreen.View providesManageScreenView() {
        return mView;
    }
}