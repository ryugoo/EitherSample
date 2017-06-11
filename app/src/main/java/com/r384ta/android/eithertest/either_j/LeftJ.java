package com.r384ta.android.eithertest.either_j;

public class LeftJ<A, B> extends EitherJ<A, B> {
    private final A a;

    public LeftJ(final A a) {
        this.a = a;
    }

    public A value() {
        return a;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public <X> X fold(FoldJ<A, X> left, FoldJ<B, X> right) {
        return left.fold(a);
    }
}
