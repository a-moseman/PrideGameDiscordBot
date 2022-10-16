package PrideBot.Concepts;

import org.jetbrains.annotations.NotNull;

public class LongLong implements Comparable<LongLong> {
    private final static long ALPHA_MAX = 1000000000000000000L; //999999999999999999L;
    private final static long MAX_ALPHA_PADDING = 18;

    public final static LongLong ZERO = new LongLong(0, 0);
    public final static LongLong ONE = new LongLong(1, 0);
    public final static LongLong TWO = new LongLong(2, 0);
    public final static LongLong TEN = new LongLong(10, 0);
    public final static LongLong MAX = new LongLong(ALPHA_MAX, Long.MAX_VALUE);

    private long alpha;
    private long beta;

    public LongLong(long alpha) {
        assert alpha >= 0;
        add(alpha);
    }

    public LongLong(long alpha, long beta) {
        assert alpha >= 0 && beta >= 0;
        add(alpha);
        this.beta = beta;
    }

    private long alphaToNextBeta() {
        return ALPHA_MAX - alpha;
    }

    public void add(long addition) {
        assert addition >= 0;
        if (addition >= alphaToNextBeta()) {
            beta++;
            alpha = alphaToNextBeta() - addition;
        } else {
            alpha += addition;
        }
    }

    public void add(LongLong addition) {
        add(addition.alpha);
        beta += addition.beta;
    }

    public void subtract(long subtraction) {
        assert subtraction >= 0;
        if (subtraction > alpha) {
            beta--;
            if (beta < 0) {
                beta = 0;
                alpha = 0;
                return;
            }
            alpha = ALPHA_MAX - (subtraction - alpha - 2);
            if (alpha < 0) {
                alpha = 0;
                return;
            }
        }
        alpha -= subtraction;
    }

    public void subtract(LongLong subtraction) {
        subtract(subtraction.alpha);
        beta -= subtraction.beta;
    }

    private String paddedAlpha() {
        if (beta == 0) {
            return "" + alpha;
        }
        String base = "" + alpha;
        String out = base;
        for (int i = 0; i < MAX_ALPHA_PADDING - base.length(); i++) {
            out = '0' + out;
        }
        return out;
    }

    public long getAlpha() {
        return alpha;
    }

    public long getBeta() {
        return beta;
    }

    @Override
    public String toString() {
        return (beta > 0 ? "" + beta : "") + paddedAlpha();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LongLong) {
            LongLong other = (LongLong) o;
            return other.alpha == alpha && other.beta == beta;
        }
        return super.equals(o);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new LongLong(alpha, beta);
    }

    @Override
    public int compareTo(@NotNull LongLong o) {
        if (o.beta < beta) {
            return 1;
        }
        else if (o.beta > beta) {
            return -1;
        }
        else {
            if (o.alpha < alpha) {
                return 1;
            }
            else if (o.alpha > alpha) {
                return -1;
            }
            return 0;
        }
    }

    public static void main(String[] args) {
        LongLong a = new LongLong(ALPHA_MAX - 1);
        System.out.println(a);
        a.add(1);
        System.out.println(a);
        a.subtract(1);
        System.out.println(a);
        a.add(200);
        System.out.println(a);
        a.subtract(2);
        System.out.println(a);
    }
}
