package com.example.aurora.moviesineedtowatch.rxjava;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 12/05/19
 * Time: 12:48
 */
public interface RxJava2ApiCallback<T> {
    void onSuccess(T t);
    void onFailed(Throwable throwable);
}
