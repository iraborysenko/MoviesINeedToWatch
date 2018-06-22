package com.example.aurora.moviesineedtowatch.dagger.module;

import com.example.aurora.moviesineedtowatch.dagger.wishlist.DatabaseRealm;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishListImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:40
 */
@Module
public class WishListModule {

    @Provides
    @Singleton
    public WishList provideWishList() {
        return new WishListImpl();
    }

    @Provides
    @Singleton
    public DatabaseRealm provideDatabaseRealm() {
        return new DatabaseRealm();
    }
}
