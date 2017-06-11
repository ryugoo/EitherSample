package com.r384ta.android.eithertest.either_j;

import android.util.Log;

import io.reactivex.Observable;

public class TestJ {
    public void test() {
        Observable<EitherJ<Throwable, String>> observable = Observable.create(emitter -> {
            emitter.onNext(EitherJ.left(new RuntimeException()));
            emitter.onNext(EitherJ.right("Hello, world"));
        });

        observable.subscribe(either -> either.fold(failure -> {
            failure.printStackTrace();
            return null;
        }, text -> {
            Log.d("TestJ", text);
            return null;
        }), error -> {
            error.printStackTrace();
        });
    }
}
