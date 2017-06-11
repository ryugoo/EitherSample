package com.r384ta.android.eithertest.either_j;

public class RightJ<A, B> extends EitherJ<A, B> {
    private final B b;

    public RightJ(final B b) {
        this.b = b;
    }

    public B value() {
        return b;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public <X> X fold(FoldJ<A, X> left, FoldJ<B, X> right) {
        return right.fold(b);
    }
}
