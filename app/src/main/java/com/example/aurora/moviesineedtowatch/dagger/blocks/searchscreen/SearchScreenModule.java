package com.example.aurora.moviesineedtowatch.dagger.blocks.searchscreen;

import com.example.aurora.moviesineedtowatch.dagger.module.NetModule;
import com.example.aurora.moviesineedtowatch.dagger.module.WishListModule;
import com.example.aurora.moviesineedtowatch.ui.search.SearchScreen;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 19/07/18
 * Time: 15:55
 */
@Module(includes = {WishListModule.class, NetModule.class})
public class SearchScreenModule {
    private final SearchScreen.View mView;

    public SearchScreenModule(SearchScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    SearchScreen.View providesSearchScreenView() {
        return mView;
    }
}
