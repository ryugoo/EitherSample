package com.r384ta.android.eithertest.either_j;

public abstract class EitherJ<A, B> {
    public static <A, B> EitherJ<A, B> left(A a) {
        return new LeftJ<>(a);
    }

    public static <A, B> EitherJ<A, B> right(B b) {
        return new RightJ<>(b);
    }

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract <X> X fold(final FoldJ<A, X> left, final FoldJ<B, X> right);
}
