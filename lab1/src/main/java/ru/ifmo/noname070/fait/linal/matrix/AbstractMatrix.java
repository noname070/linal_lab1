package ru.ifmo.noname070.fait.linal.matrix;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ru.ifmo.noname070.fait.linal.matrix.bonus.BaseMatrix;
import ru.ifmo.noname070.fait.linal.utils.Pair;

public abstract class AbstractMatrix implements IMatrix {
    private final Pair<Integer, Integer> size; // el1=n, el2=m
    private final String SEPARATOR = " -+- ";

    public AbstractMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Размерности обязаны быть >0");
        }
        this.size = new Pair<>(rows, cols);
    }

    protected abstract IMatrix createMatrix(int rows, int cols);

    public abstract Double getElement(int row, int col);

    public abstract void setElement(int row, int col, double value);

    public abstract IMatrix add(IMatrix o);

    public abstract IMatrix mul(IMatrix o);

    public abstract IMatrix mulScalar(double s);

    protected void validateIdxs(int row, int col) {
        if (row <= 0 || row > getSize().el1()
                || col <= 0 || col > getSize().el2()) {
            throw new IndexOutOfBoundsException(
                    String.format("Некорректные индексы элемента %d x %d, в то время как матрица размером %d x %d",
                            row, col, getSize().el1(), getSize().el2()));
        }
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
            return this.getElement(1, 1)
                    * this.getElement(2, 2)
                    - this.getElement(1, 2)
                            * this.getElement(2, 1);
        }

        double d = 0d;
        for (int j = 1; j <= n; j++) {
            d += Math.pow(-1, j - 1)
                    * this.getElement(1, j)
                    * this.minor(1, j).det();
        }

        return d;
    }

    protected IMatrix minor(int exR, int exC) {
        // IMatrix result = this.createMatrix(this.getSize().el1(), getSize().el2());
        // а тут оно иначе минором не считается
        IMatrix result = new BaseMatrix(this.getSize().el1() - 1, this.getSize().el2() - 1);
        int newR = 1;

        for (int i = 1; i <= this.getSize().el1(); i++) {
            if (i == exR)
                continue;

            int newC = 1;
            for (int j = 1; j <= this.getSize().el2(); j++) {
                if (j == exC)
                    continue;

                result.setElement(newR, newC, this.getElement(i, j));
                newC++;
            }
            newR++;
        }

        return result;
    }

    public Pair<Integer, Integer> getSize() {
        return this.size;
    }

    public boolean hasInverse() {
        if (!this.isSquare()) {
            throw new UnsupportedOperationException("Обратить можно только квадратные матрицы");
        }
        return this.det() != 0;
    }

    public boolean isSquare() {
        return size.el1().equals(size.el2());
    }

    @Override
    public String toString() {
        return IntStream.range(0, size.el1())
                .mapToObj(row -> IntStream.range(0, size.el2())
                        .mapToObj(col -> this.getElement(row, col).toString())
                        .collect(Collectors.joining(SEPARATOR)))
                .collect(Collectors.joining("\n"));
    }
}
