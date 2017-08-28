package ru.ifmo.nds.util;

public final class DominanceHelper {
    private DominanceHelper() {}

    public static final int HAS_LESS_MASK = 1;
    public static final int HAS_GREATER_MASK = 2;

    public static int dominanceComparison(double[] a, double[] b, int breakMask) {
        int dim = a.length;
        int result = 0;
        for (int i = 0; i < dim; ++i) {
            double ai = a[i], bi = b[i];
            if (ai < bi) {
                result |= HAS_LESS_MASK;
            } else if (ai > bi) {
                result |= HAS_GREATER_MASK;
            }
            if ((result & breakMask) == breakMask) {
                break;
            }
        }
        return result;
    }
}
