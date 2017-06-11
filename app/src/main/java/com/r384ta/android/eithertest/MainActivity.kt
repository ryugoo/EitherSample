package com.r384ta.android.eithertest

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.r384ta.android.eithertest.databinding.ActivityMainBinding
import com.r384ta.android.eithertest.either_j.EitherJ
import com.r384ta.android.eithertest.either_k.*
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainActivity : RxAppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.testEitherJ.setOnClickListener {
            testEitherJ()
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ either ->
                    either.fold({
                        Timber.d("Is left ? = ${either.isLeft}")
                        Timber.d(it.message ?: "message is empty")
                    }, {
                        Timber.d("Is right ? = ${either.isRight}")
                        Timber.d(it)
                    })
                }, {
                    Timber.d(it.message ?: "message is empty")
                })
        }

        binding.testEitherK.setOnClickListener {
            Observable.merge(testObservable().either(), testEitherK())
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ either ->
                    either.fold({
                        Timber.d("Is left ? = ${either.isLeft}")
                        Timber.d(it.message ?: "message is empty")
                    }, {
                        Timber.d("Is right ? = ${either.isRight}")
                        Timber.d(it)
                    })
                }, {
                    Timber.d(it.message ?: "message is empty")
                })
        }

        RxView.clicks(binding.testRxBinding)
            .compose(bindToLifecycle())
            .map { throw IllegalStateException() }
            .flatMap { Observable.error<String>(IllegalStateException()) }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("All right")
            }, Timber::e)

        RxView.clicks(binding.testRxBindingEither)
            .compose(bindToLifecycle())
            .map { LeftK<Throwable, String>(IllegalStateException()) }
            .flatMap { Observable.just<LeftK<Throwable, String>>(LeftK(IllegalStateException())) }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("All right")
            }, Timber::e)

        testObservable().either().subscribe({ either ->
            either.fold({

            }, {

            })
        }, {

        })
    }

    fun testEitherJ(): Observable<EitherJ<Throwable, String>> = Observable.create {
        it.onNext(EitherJ.left(IllegalStateException("Failure 1 (Java)")))
        it.onNext(EitherJ.left(IllegalStateException("Failure 2 (Java)")))
        it.onNext(EitherJ.left(IllegalStateException("Failure 3 (Java)")))
        it.onNext(EitherJ.right("Success 1 (Java)"))
        it.onNext(EitherJ.right("Success 2 (Java)"))
        it.onNext(EitherJ.right("Success 3 (Java)"))
        // it.onError(IllegalStateException("Error (Java)"))
    }

    fun testEitherK(): Observable<EitherK<Throwable, String>> = Observable.create {
        it.onNext(LeftK(IllegalStateException("Failure 1 (Kotlin)")))
        it.onNext(LeftK(IllegalStateException("Failure 2 (Kotlin)")))
        it.onNext(LeftK(IllegalStateException("Failure 3 (Kotlin)")))
        it.onNext(RightK("Success 1 (Kotlin)"))
        it.onNext(RightK("Success 2 (Kotlin)"))
        it.onNext(RightK("Success 3 (Kotlin)"))
        // it.onError(IllegalStateException("Error (Kotlin)"))
    }

    fun testObservable(): Observable<String> = Observable.create {
        it.onNext("Success (Test)")
        it.onError(IllegalStateException("Error (Test)"))
    }
}
