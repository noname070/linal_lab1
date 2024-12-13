
package ru.ifmo.noname070.fait.linal.matrix.bonus;

import ru.ifmo.noname070.fait.linal.matrix.IMatrix;
import ru.ifmo.noname070.fait.linal.utils.Pair;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.IntStream;

public class FastPowOf2Matrix extends BaseMatrix implements IFastMatrix {

    private final ConcurrentMap<Integer, ConcurrentMap<Integer, Double>> data;

    public FastPowOf2Matrix(int rows, int сols) {
        super(rows, сols);
        this.validateSize(rows, сols);
        this.data = new ConcurrentHashMap<>();
    }

    public FastPowOf2Matrix(Pair<Integer, Integer> size) {
        this(size.el1(), size.el2());
    }

    @Override
    protected IMatrix createMatrix(int rows, int cols) {
        return new FastPowOf2Matrix(rows, cols);
    }

    private void validateSize(int rows, int cols) {
        if ((rows != cols) || (!isPowOf2(rows))) {
            throw new IllegalArgumentException(
                    "Некорректный размер - матрица должна быть квадратная и размерности должеы быть равны n^2");
        }
    }

    private boolean isPowOf2(int n) {
        return (n != 0) && ((n & (n - 1)) == 0);
    }

    @Override
    public void setElement(int row, int col, double value) {
        data.computeIfAbsent(row - 1, k -> new ConcurrentHashMap<>()).put(col - 1, value);
    }

    @Override
    public Double getElement(int row, int col) {
        return data.getOrDefault(row - 1, new ConcurrentHashMap<>()).getOrDefault(col - 1, 0d);
    }

    @Override
    public IMatrix add(IMatrix o) {
        if (!(o instanceof IFastMatrix)) {
            return super.add(o);
        }

        IFastMatrix result = new FastPowOf2Matrix(this.getSize());

        IntStream.range(0, this.getSize().el1()).forEach(i -> {
            IntStream.range(0, this.getSize().el1()).parallel().forEach(j -> {
                result.setElement(i, j, this.getElement(i, j) + o.getElement(i, j));
            });
        });

        return result;
    }

    @Override
    public IMatrix mul(IMatrix o) {
        if (!(o instanceof IFastMatrix)) {
            return super.mul(o);
        }

        IFastMatrix result = new FastPowOf2Matrix(this.getSize());

        IntStream.range(0, this.getSize().el1()).forEach(i -> {
            IntStream.range(0, this.getSize().el2()).parallel().forEach(j -> {
                double s = 0;
                for (int k = 0; k < this.getSize().el1(); k++) {
                    s += this.getElement(i, k) * o.getElement(k, j);
                }
                result.setElement(i, j, s);
            });
        });
        return result;
    }

    @Override
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
                    * this.getElement(2, j)
                    * this.minor(2, j).det();
        }

        return d;
    }

    @Override
    public IMatrix mulScalar(double s) {
        IMatrix result = createMatrix(this.getSize().el1(), this.getSize().el2());

        IntStream.range(0, this.getSize().el1()).forEach(i -> {
            IntStream.range(0, this.getSize().el2()).parallel().forEach(j -> {
                result.setElement(i, j, s * this.getElement(i, j));
            });
        });

        return result;
    }
}