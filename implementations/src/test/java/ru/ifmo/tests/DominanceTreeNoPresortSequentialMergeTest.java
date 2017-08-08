package ru.ifmo.tests;

import ru.ifmo.DominanceTree;
import ru.ifmo.NonDominatedSortingFactory;

public class DominanceTreeNoPresortSequentialMergeTest extends CorrectnessTestsBase {
    @Override
    protected NonDominatedSortingFactory getFactory() {
        return DominanceTree.getNoPresortInsertion(false);
    }
}