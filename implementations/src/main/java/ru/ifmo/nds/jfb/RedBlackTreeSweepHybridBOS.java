package ru.ifmo.nds.jfb;

import ru.ifmo.nds.bos.ImprovedAdaptedForHybrid;

public class RedBlackTreeSweepHybridBOS extends RedBlackTreeSweep {
    private final ImprovedAdaptedForHybrid bos;
    private double[][] tempPoints;
    private int[] tempRanks;

    private static final int THRESHOLD_3D = 20;
    private static final int THRESHOLD_ALL = 200;

    public RedBlackTreeSweepHybridBOS(int maximumPoints, int maximumDimension, int allowedThreads) {
        super(maximumPoints, maximumDimension, allowedThreads);
        bos = new ImprovedAdaptedForHybrid(maximumPoints, maximumDimension);
        tempPoints = new double[maximumPoints][maximumDimension];
        tempRanks = new int[maximumPoints];
    }

    @Override
    public String getName() {
        return "Jensen-Fortin-Buzdalov sorting, "
                + getThreadDescription()
                + " (tree sweep, hybrid with Best Order Sort)";
    }

    @Override
    protected boolean helperAHookCondition(int size, int obj) {
        switch (obj) {
            case 1: return false;
            case 2: return size < THRESHOLD_3D;
            default: return size < THRESHOLD_ALL;
        }
    }

    @Override
    protected int helperAHook(int from, int until, int obj) {
        getPoints(from, until, obj + 1, tempPoints);
        getRanks(from, until, tempRanks);

        bos.sortCheckedWithRespectToRanks(
                tempPoints,
                tempRanks,
                until - from,
                obj + 1,
                maximalMeaningfulRank);

        for (int i = from; i < until; i++) {
            ranks[indices[i]] = tempRanks[i - from];
        }
        return until;
    }
}
