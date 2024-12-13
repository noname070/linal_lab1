package ru.ifmo.noname070.fait.linal.matrix;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.ifmo.noname070.fait.linal.matrix.bonus.BaseMatrix;
import ru.ifmo.noname070.fait.linal.utils.Pair;

public class SparseMatrix extends BaseMatrix {
    protected final Map<Integer, Map<Integer, Double>> data;

    public SparseMatrix(int sizeX, int sizeY) {
        super(sizeX, sizeY);
        this.data = new LinkedHashMap<>();
    }

    public SparseMatrix(Pair<Integer, Integer> size) {
        this(size.el1(), size.el2());
    }

    // protected Map<Integer, Map<Integer, Double>> getData() {return data;}

    @Override
    protected IMatrix createMatrix(int rows, int cols) {
        return new SparseMatrix(rows, cols);
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
        data.computeIfAbsent(row - 1, k -> new LinkedHashMap<>());
        if (value == 0.0) {
            data.get(row - 1).remove(col - 1);
            if (data.get(row - 1).isEmpty()) {
                data.remove(row - 1);
            }
        } else {
            data.getOrDefault(row - 1, new LinkedHashMap<>()).put(col - 1, value);
        }
    }

    @Override
    public IMatrix add(IMatrix o) {
        if (!this.getSize().equals(o.getSize())) {
            throw new IllegalArgumentException("Эти матрицы не могут быть перемножены");
        }

        if (!(o instanceof SparseMatrix)) {
            return super.add(o);
        }

        IMatrix result = createMatrix(this.getSize().el1(), this.getSize().el2());
        this.data.forEach((row, cols) -> cols.forEach((col, value) -> result.setElement(row + 1, col + 1, value)));

        ((SparseMatrix) o).data.forEach((row, cols) -> cols.forEach((col, value) -> {
            result.setElement(row + 1, col + 1, result.getElement(row + 1, col + 1) + value);
        }));

        return result;
    }

    @Override
    public IMatrix mul(IMatrix o1) {
        if (!this.getSize().el2().equals(o1.getSize().el1())) {
            throw new IllegalArgumentException("Эти матрицы не могут быть перемножены");
        }

        if (!(o1 instanceof SparseMatrix)) {
            return super.mul(o1);
        }

        SparseMatrix o = (SparseMatrix) o1;
        IMatrix result = this.createMatrix(this.getSize().el1(), o.getSize().el2());

        for (Map.Entry<Integer, Map<Integer, Double>> rowEntry : this.data.entrySet()) {
            for (int col = 1; col <= o.getSize().el2(); col++) {

                double s = 0.0;
                for (Map.Entry<Integer, Double> rowES : rowEntry.getValue().entrySet()) {
                    if (o.data.containsKey(rowES.getKey())) {
                        if (o.data.get(rowES.getKey()).containsKey(col - 1)) {
                            s += rowES.getValue() * o.data.get(rowES.getKey()).get(col - 1);
                        }
                    }
                }

                if (s != 0.0) {
                    result.setElement(rowEntry.getKey() + 1, col, s);
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

    public double calculateTrace() {
        if (!isSquare()) {
            throw new UnsupportedOperationException("След можно посчитать только для квадратных матриц");
        }

        return this.data.entrySet().stream()
                .filter(e -> e.getValue().containsKey(e.getKey()))
                .mapToDouble(e -> e.getValue().get(e.getKey()))
                .sum();
    }
}
