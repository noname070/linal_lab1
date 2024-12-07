package ru.ifmo.noname070.fait.linal.matrix;

import java.util.HashMap;
import java.util.Map;

import ru.ifmo.noname070.fait.linal.utils.Pair;

/*
 * могло работать и быстрее, но тут гибкое решение,
 * которое будет работать на всех реализациях IMatrix
 */
public class SparseMatrix extends AbstractMatrix {

    protected final Map<Integer, Map<Integer, Double>> data;

    public SparseMatrix(int sizeX, int sizeY) {
        super(sizeX, sizeY);
        this.data = new HashMap<>();
    }

    public SparseMatrix(Pair<Integer, Integer> size) {
        this(size.el1(), size.el2());
    }

    public Map<Integer, Map<Integer, Double>> getData() {
        return this.data;
    }

    @Override
    public Double getElement(int row, int col) {
        this.validateIdxs(row, col);
        return data.getOrDefault(row - 1, Map.of())
                .getOrDefault(col - 1, 0d);
    }

    @Override
    public void setElement(int row, int col, double value) {
        this.validateIdxs(row, col);
        data.computeIfAbsent(row - 1, k -> new HashMap<>());
        if (value == 0.0) {
            data.get(row - 1).remove(col - 1);
            if (data.get(row - 1).isEmpty()) {
                data.remove(row - 1);
            }
        } else {
            data.getOrDefault(row - 1, new HashMap<>()).put(col - 1, value);
        }
    }

    @Override
    public IMatrix add(IMatrix o) {
        if (!this.getSize().equals(o.getSize())) {
            throw new IllegalArgumentException("Эти матрицы нельзя сложить");
        }

        IMatrix result = new SparseMatrix(getSize());

        for (int i = 1; i <= getSize().el1(); i++) {
            for (int j = 1; j <= getSize().el2(); j++) {
                double value = this.getElement(i, j) + o.getElement(i, j);
                if (value != 0.0) {
                    result.setElement(i, j, value);
                }
            }
        }

        return result;
    }

    @Override
    public IMatrix mul(IMatrix o) {
        if (this.getSize().el2() != o.getSize().el1()) {
            throw new IllegalArgumentException("Эти матрицы не могут быть перемножены");
        }

        IMatrix result = new SparseMatrix(this.getSize().el1(), o.getSize().el2());

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
        SparseMatrix result = new SparseMatrix(getSize());
        this.data.forEach((row, cols) -> cols.forEach((col, value) -> result.setElement(row + 1, col + 1, value * s)));
        return result;
    }

    protected void validateIdxs(int row, int col) {
        if (row <= 0 || row > getSize().el1()
                || col <= 0 || col > getSize().el2()) {
            throw new IndexOutOfBoundsException(
                    String.format("Некорректные индексы элемента (%d, %d), в то время как матрица размером %d x %d",
                            row, col, getSize().el1(), getSize().el2()));
        }
    }

    public double calculateTrace() {
        if (!isSquare()) {
            throw new UnsupportedOperationException("След можно посчитать только для квадратных матриц");
        }

        return this.data.entrySet().stream()
                .filter(e -> e.getValue().containsKey(e.getKey()))
                .mapToDouble(e -> e.getValue().get(e.getKey()))
                .sum();
    }

    public double det() {
        if (!this.isSquare()) {
            throw new UnsupportedOperationException("Детерминант можно посчитать только для квадратных матриц");
        }

        int n = getSize().el1();
        if (n == 1) {
            return this.getElement(1, 1);
        }

        if (n == 2) {
            return this.getElement(1, 1) * this.getElement(2, 2) - this.getElement(1, 2) * this.getElement(2, 1);
        }

        double determinant = 0d;
        for (int j = 1; j <= n; j++) {
            determinant += Math.pow(-1, j - 1) * this.getElement(1, j) * this.minor(1, j).det();
        }

        return determinant;
    }

    private SparseMatrix minor(int excludeRow, int excludeCol) {
        SparseMatrix result = new SparseMatrix(getSize().el1() - 1, getSize().el2() - 1);
        int newRow = 1;

        for (int i = 1; i <= getSize().el1(); i++) {
            if (i == excludeRow)
                continue;

            int newCol = 1;
            for (int j = 1; j <= getSize().el2(); j++) {
                if (j == excludeCol)
                    continue;

                result.setElement(newRow, newCol, this.getElement(i, j));
                newCol++;
            }
            newRow++;
        }

        return result;
    }

    public boolean hasInverse() {
        if (!this.isSquare()) {
            throw new UnsupportedOperationException("Обратить можно только квадратные матрицы");
        }
        return this.det() != 0;
    }

}
