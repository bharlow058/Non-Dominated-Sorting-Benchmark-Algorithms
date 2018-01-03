package ru.ifmo.nds.ndt;

import java.util.Arrays;

import ru.ifmo.nds.NonDominatedSorting;
import ru.ifmo.nds.util.ArrayHelper;
import ru.ifmo.nds.util.DoubleArraySorter;

public class ENS_NDT extends NonDominatedSorting {
    private static final int THRESHOLD = 50;

    private DoubleArraySorter sorter;
    private SplitBuilder splitBuilder;
    private TreeNode[] levels;
    private int[] indices;
    private int[] ranks;
    private double[][] transposedPoints;
    private double[][] points;

    public ENS_NDT(int maximumPoints, int maximumDimension) {
        super(maximumPoints, maximumDimension);
        this.sorter = new DoubleArraySorter(maximumPoints);
        this.splitBuilder = new SplitBuilder(maximumPoints);
        this.levels = new TreeNode[maximumPoints];
        this.indices = new int[maximumPoints];
        this.ranks = new int[maximumPoints];
        this.transposedPoints = new double[maximumDimension][maximumPoints];
        this.points = new double[maximumPoints][];
    }

    @Override
    public String getName() {
        return "ENS-NDT";
    }

    @Override
    protected void closeImpl() {
        sorter = null;
        splitBuilder = null;
        levels = null;
        indices = null;
        ranks = null;
        transposedPoints = null;
        points = null;
    }

    @Override
    protected void sortChecked(double[][] points, int[] ranks, int maximalMeaningfulRank) {
        int n = points.length;
        int dim = points[0].length;
        ArrayHelper.fillIdentity(indices, n);
        sorter.lexicographicalSort(points, indices, 0, n, points[0].length);
        int newN = DoubleArraySorter.retainUniquePoints(points, indices, this.points, ranks);
        Arrays.fill(this.ranks, 0, newN, 0);

        for (int i = 0; i < newN; ++i) {
            levels[i] = TreeNode.EMPTY;
            for (int j = 0; j < dim; ++j) {
                transposedPoints[j][i] = this.points[i][j];
            }
        }

        Split split = splitBuilder.result(transposedPoints, newN, dim, THRESHOLD);

        int maxRank = 1;
        levels[0] = levels[0].add(this.points[0], split, THRESHOLD);
        for (int i = 1; i < newN; ++i) {
            double[] current = this.points[i];
            if (levels[0].dominates(current, split)) {
                int left = 0, right = maxRank;
                while (right - left > 1) {
                    int mid = (left + right) >>> 1;
                    if (levels[mid].dominates(current, split)) {
                        left = mid;
                    } else {
                        right = mid;
                    }
                }
                int rank = left + 1;
                this.ranks[i] = rank;
                if (rank <= maximalMeaningfulRank) {
                    levels[rank] = levels[rank].add(current, split, THRESHOLD);
                    if (rank == maxRank) {
                        ++maxRank;
                    }
                }
            } else {
                levels[0] = levels[0].add(current, split, THRESHOLD);
            }
        }

        for (int i = 0; i < n; ++i) {
            ranks[i] = this.ranks[ranks[i]];
            this.points[i] = null;
        }
    }
}