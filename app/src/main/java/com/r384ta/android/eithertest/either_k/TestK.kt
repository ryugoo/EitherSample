package com.r384ta.android.eithertest.either_k

import io.reactivex.Observable
import timber.log.Timber

fun test() {
    val observable: Observable<EitherK<Throwable, String>> = Observable.create {
        it.onNext(LeftK(RuntimeException()))
        it.onNext(RightK("Hello, world"))
    }

    observable.subscribe({ either ->
        either.fold({
            Timber.e(it)
        }, {
            Timber.d(it)
        })
    }, {
        it.printStackTrace()
    })
}

