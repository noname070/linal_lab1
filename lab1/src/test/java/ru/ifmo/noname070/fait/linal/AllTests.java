package ru.ifmo.noname070.fait.linal;

import ru.ifmo.noname070.fait.linal.matrix.SparseMatrixTest;
import ru.ifmo.noname070.fait.linal.matrix.bonus.BaseMatrixTest;
import ru.ifmo.noname070.fait.linal.matrix.bonus.FastPowOf2MatrixTest;

/*
 * ~50% тестов сгенерировано (мне лень писать (на самом деле - больше xd))
 * микрофрейм для тестов писал сам :умни
 */
public class AllTests {
    
    public static void main(String[] args) {
        BaseMatrixTest.main(args);
        SparseMatrixTest.main(args);
        FastPowOf2MatrixTest.main(args);
    }
}
