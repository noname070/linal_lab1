package ru.ifmo.noname070.fait.linal.matrix.bonus;

import java.util.stream.IntStream;

import ru.ifmo.noname070.fait.linal.matrix.AbstractMatrix;
import ru.ifmo.noname070.fait.linal.matrix.IMatrix;
import ru.ifmo.noname070.fait.linal.utils.Pair;

public class BaseMatrix extends AbstractMatrix {

    protected final double[][] data;

    public BaseMatrix(int rows, int cols) {
        super(rows, cols);
        this.data = new double[rows + 1][cols + 1];
    }

    public BaseMatrix(Pair<Integer, Integer> size) {
        this(size.el1(), size.el2());
    }

    @Override
    protected IMatrix createMatrix(int rows, int cols) {
        return new BaseMatrix(rows, cols);
    }

    @Override
    public Double getElement(int row, int col) {
        return this.data[row - 1][col - 1];
    }

    @Override
    public void setElement(int row, int col, double value) {
        this.data[row - 1][col - 1] = value;
    }

    @Override
    public IMatrix add(IMatrix o) {
        if (!this.getSize().equals(o.getSize())) {
            throw new IllegalArgumentException("Эти матрицы нельзя сложить");
        }

        IMatrix result = new BaseMatrix(this.getSize());

        for (int i = 1; i <= this.getSize().el1(); i++) {
            for (int j = 1; j <= this.getSize().el2(); j++) {
                double sum = this.getElement(i, j) + o.getElement(i, j);
                result.setElement(i, j, sum);
            }
        }

        return result;
    }

    @Override
    public IMatrix mul(IMatrix o) {
        if (!this.getSize().el2().equals(o.getSize().el1())) {
            throw new IllegalArgumentException("Эти матрицы не могут быть перемножены");
        }

        IMatrix result = new BaseMatrix(this.getSize().el1(), o.getSize().el2());

        for (int i = 1; i <= this.getSize().el1(); i++) {
            for (int j = 1; j <= o.getSize().el2(); j++) {
                double sum = 0d;
                for (int k = 1; k <= this.getSize().el2(); k++) {
                    sum += this.getElement(i, k) * o.getElement(k, j);
                }
                if (sum != 0d) {
                    result.setElement(i, j, sum);
                }
            }
        }

        return result;
    }

    @Override
    public IMatrix mulScalar(double s) {
        IMatrix result = createMatrix(this.getSize().el1(), this.getSize().el2());

        IntStream.range(1, this.data.length)
                .forEach(row -> IntStream.range(1, this.data[row].length)
                        .forEach(col -> result.setElement(row, col, s * this.getElement(row, col))));

        return result;
    }

}
