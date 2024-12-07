package ru.ifmo.noname070.fait.linal.matrix;

import ru.ifmo.noname070.fait.linal.utils.Pair;

public interface IMatrix {
    Pair<Integer, Integer> getSize();

    Double getElement(int row, int col);

    void setElement(int row, int col, double value);

    IMatrix add(IMatrix o);

    IMatrix mul(IMatrix o);

    IMatrix mulScalar(double s);

}
