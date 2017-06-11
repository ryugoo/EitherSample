package com.r384ta.android.eithertest.either_k

import io.reactivex.Observable

@Suppress("unused")
sealed class EitherK<out A, out B> {
    abstract val isLeft: Boolean
    abstract val isRight: Boolean
}

data class LeftK<out A, out B>(val value: A) : EitherK<A, B>() {
    override val isLeft: Boolean = true
    override val isRight: Boolean = false
}

data class RightK<out A, out B>(val value: B) : EitherK<A, B>() {
    override val isLeft: Boolean = false
    override val isRight: Boolean = true
}

inline fun <A, B, X> EitherK<A, B>.fold(left: (A) -> X, right: (B) -> X): X = when (this) {
    is LeftK -> left(value)
    is RightK -> right(value)
}

typealias RxEither<T> = EitherK<Throwable, T>
typealias RxRight<T> = RightK<Throwable, T>
typealias RxLeft<T> = LeftK<Throwable, T>

@Suppress("USELESS_CAST")
fun <T> Observable<T>.either(): Observable<RxEither<T>> =
    map { RxRight(it) as RxEither<T> }
    .onErrorReturn { RxLeft(it) }