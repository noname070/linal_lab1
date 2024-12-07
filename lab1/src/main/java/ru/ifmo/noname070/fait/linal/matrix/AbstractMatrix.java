package ru.ifmo.noname070.fait.linal.matrix;

import ru.ifmo.noname070.fait.linal.utils.Pair;

public abstract class AbstractMatrix implements IMatrix {
    private final Pair<Integer, Integer> size; // el1=n, el2=m
    private final String SEPARATOR = " ";

    public AbstractMatrix(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Размерности обязаны быть >0");
        }
        this.size = new Pair<>(rows, columns);
    }

    public abstract Double getElement(int row, int col);

    public abstract void setElement(int row, int col, double value);

    public abstract IMatrix add(IMatrix o);

    public abstract IMatrix mul(IMatrix o);

    public abstract IMatrix mulScalar(double s);

    public Pair<Integer, Integer> getSize() {
        return this.size;
    }

    public boolean isSquare() {
        return this.size.el1().equals(this.size.el2());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int n = 1; n <= size.el1(); n++) {
            for (int m = 1; m <= size.el2(); m++) {
                sb.append(getElement(n, m))
                        .append(SEPARATOR);
            }
            if (n < size.el1()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
