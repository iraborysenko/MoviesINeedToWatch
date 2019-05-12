package com.example.aurora.moviesineedtowatch.rxjava;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 12/05/19
 * Time: 12:49
 */
public class RxJava2FlowableApiCallHelper {
    public static <T> Disposable call(Flowable<T> observable, final RxJava2ApiCallback<T> rxApiCallback) {
        if (observable == null) {
            throw new IllegalArgumentException("Observable must not be null.");
        }
        if (rxApiCallback == null) {
            throw new IllegalArgumentException("Callback must not be null.");
        }

        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxApiCallback::onSuccess, throwable -> {
                    if (throwable != null) {
                        rxApiCallback.onFailed(throwable);
                    } else {
                        rxApiCallback.onFailed(new Throwable("Something went wrong"));
                    }
                });
    }
}
