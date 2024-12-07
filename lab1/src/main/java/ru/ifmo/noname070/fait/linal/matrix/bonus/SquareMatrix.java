package ru.ifmo.noname070.fait.linal.matrix.bonus;

import ru.ifmo.noname070.fait.linal.utils.Pair;

public class SquareMatrix extends BaseMatrix {

    public SquareMatrix(int rows, int columns) {
        super(rows, columns);
        if (rows != columns) {
            throw new IllegalArgumentException("Размерности в квадратных матрицах должны быть равны");
        }
    }

    public SquareMatrix(Pair<Integer, Integer> size) {
        this(size.el1(), size.el2());
    }
    
}
