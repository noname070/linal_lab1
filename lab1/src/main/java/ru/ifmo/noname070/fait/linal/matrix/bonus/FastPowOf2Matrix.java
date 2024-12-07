package ru.ifmo.noname070.fait.linal.matrix.bonus;

import ru.ifmo.noname070.fait.linal.matrix.IMatrix;
import ru.ifmo.noname070.fait.linal.matrix.SparseMatrix;
import ru.ifmo.noname070.fait.linal.utils.Pair;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ForkJoinPool;

/*
 * бонус - на следющий день попробовал написать быстрые операции над n^2 квадратными матрицами.
 * конечно, тут с точки зрения джавы прикольно засунуть фабрику и тп тп, но пусть будет
 * 
 * пишу это уже спустя 1.5 часа - мдаа высоконагруженные вычисления это, конечно, не шутки тут шутить... лучше бы поспал нормально..
 */
public class FastPowOf2Matrix extends SparseMatrix implements IPowOf2Matrix {

    private final ConcurrentMap<Integer, ConcurrentMap<Integer, Double>> data = new ConcurrentHashMap<>();

    public FastPowOf2Matrix(int rows, int columns) {
        super(rows, columns);
        if (rows != columns || !isPowerOf2(columns) || !isPowerOf2(rows)) {
            throw new IllegalArgumentException("Размерности быстрой матрицы должны быть равны 2^n");
        }
    }

    public FastPowOf2Matrix(Pair<Integer, Integer> size) {
        this(size.el1(), size.el2());
    }

    @Override
    public Double getElement(int row, int col) {
        this.validateIdxs(row, col);
        
        return data.getOrDefault(row - 1, new ConcurrentHashMap<>())
                .getOrDefault(col - 1, 0d);
    }

    @Override
    public void setElement(int row, int col, double value) {
        this.validateIdxs(row, col);
        data.computeIfAbsent(row - 1, k -> new ConcurrentHashMap<>());
        if (value == 0.0) {
            data.get(row - 1).remove(col - 1);
            if (data.get(row - 1).isEmpty()) {
                data.remove(row - 1);
            }
        } else {
            data.getOrDefault(row - 1, new ConcurrentHashMap<>()).put(col - 1, value);
        }
    }

    private boolean isPowerOf2(int n) {
        return (n != 0) && ((n & (n - 1)) == 0);
    }

    @Override
    public IMatrix mul(IMatrix o) {
        if (!(o instanceof FastPowOf2Matrix)) {
            return super.mul(o);
        }
        return this.mul((FastPowOf2Matrix) o);
    }

    public FastPowOf2Matrix mul(FastPowOf2Matrix o) {
        if (!o.getSize().el1().equals(this.getSize().el1())) {
            throw new IllegalArgumentException("Размерности матриц не совпадают: %s x %s | %s x %s".formatted(
                o.getSize().el1(), o.getSize().el2(),
                this.getSize().el1(), this.getSize().el2()
            ));
        }

        FastPowOf2Matrix result = new FastPowOf2Matrix(this.getSize());
        this._mul(this, o, result, this.getSize().el1());
        return result;
    }

    private void _mul(FastPowOf2Matrix a, FastPowOf2Matrix b, FastPowOf2Matrix result, int size) {
        if (size == 1) {
            result.setElement(1, 1, a.getElement(1, 1) * b.getElement(1, 1));
            return;
        }

        int newSize = size / 2;

        FastPowOf2Matrix a11 = a.subMatrix(0, 0, newSize);
        FastPowOf2Matrix a12 = a.subMatrix(0, newSize, newSize);
        FastPowOf2Matrix a21 = a.subMatrix(newSize, 0, newSize);
        FastPowOf2Matrix a22 = a.subMatrix(newSize, newSize, newSize);

        FastPowOf2Matrix b11 = b.subMatrix(0, 0, newSize);
        FastPowOf2Matrix b12 = b.subMatrix(0, newSize, newSize);
        FastPowOf2Matrix b21 = b.subMatrix(newSize, 0, newSize);
        FastPowOf2Matrix b22 = b.subMatrix(newSize, newSize, newSize);

        FastPowOf2Matrix m1 = add(a11, a22).mul(add(b11, b22));
        FastPowOf2Matrix m2 = add(a21, a22).mul(b11);
        FastPowOf2Matrix m3 = a11.mul(sub(b12, b22));
        FastPowOf2Matrix m4 = a22.mul(sub(b21, b11));
        FastPowOf2Matrix m5 = add(a11, a12).mul(b22);
        FastPowOf2Matrix m6 = sub(a21, a11).mul(add(b11, b12));
        FastPowOf2Matrix m7 = sub(a12, a22).mul(add(b21, b22));

        FastPowOf2Matrix c11 = add(sub(add(m1, m4), m5), m7);
        FastPowOf2Matrix c12 = add(m3, m5);
        FastPowOf2Matrix c21 = add(m2, m4);
        FastPowOf2Matrix c22 = add(sub(add(m1, m3), m2), m6);

        result.assembleFrom(c11, c12, c21, c22, newSize);
    }

    private FastPowOf2Matrix subMatrix(int startRow, int startCol, int size) {
        FastPowOf2Matrix result = new FastPowOf2Matrix(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result.setElement(i + 1, j + 1, this.getElement(startRow + i + 1, startCol + j + 1));
            }
        }
        return result;
    }

    private FastPowOf2Matrix add(FastPowOf2Matrix a, FastPowOf2Matrix b) {
        FastPowOf2Matrix result = new FastPowOf2Matrix(a.getSize());
        ForkJoinPool.commonPool().invoke(new MAddTask(a, b, result, 0, a.getSize().el1()));
        return result;
    }

    private FastPowOf2Matrix sub(FastPowOf2Matrix a, FastPowOf2Matrix b) {
        FastPowOf2Matrix result = new FastPowOf2Matrix(a.getSize());
        ForkJoinPool.commonPool().invoke(new MSubTask(a, b, result, 0, a.getSize().el1()));
        return result;
    }

    private void assembleFrom(
            FastPowOf2Matrix c11,
            FastPowOf2Matrix c12,
            FastPowOf2Matrix c21,
            FastPowOf2Matrix c22,
            int size) {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.setElement(i + 1,          j + 1,          c11.getElement(i + 1, j + 1));
                this.setElement(i + 1,          j + size + 1,   c12.getElement(i + 1, j + 1));
                this.setElement(i + size + 1,   j + 1,          c21.getElement(i + 1, j + 1));
                this.setElement(i + size + 1,   j + size + 1,   c22.getElement(i + 1, j + 1));
            }
        }
    }

    @Override
    public double det() {
        int size = this.getSize().el1();
        if (size == 1) {
            return this.getElement(1, 1);
        }

        double determinant = 0;
        for (int col = 0; col < size; col++) {
            determinant += ((col % 2 == 0) ? 1 : -1)
                    * this.getElement(1, col + 1)
                    * this.minor(1, col + 1).det();
        }

        return determinant;
    }

    private FastPowOf2Matrix minor(int row, int col) {
        int size = this.getSize().el1();
        FastPowOf2Matrix result = new FastPowOf2Matrix(size - 1, size - 1);

        for (int i = 0, newRow = 0; i < size; i++) {
            if (i == row - 1)
                continue;
            for (int j = 0, newCol = 0; j < size; j++) {
                if (j == col - 1)
                    continue;
                result.setElement(newRow + 1, newCol + 1, this.getElement(i + 1, j + 1));
                newCol++;
            }
            newRow++;
        }

        return result;
    }

    // public IPowOf2Matrix add(IPowOf2Matrix o) {
    //     return this.add((FastPowOf2Matrix) o);
    // }

    private static class MAddTask extends RecursiveAction {
        private final FastPowOf2Matrix a;
        private final FastPowOf2Matrix b;
        private final FastPowOf2Matrix result;

        private final int start;
        private final int end;

        public MAddTask(
                FastPowOf2Matrix a,
                FastPowOf2Matrix b,
                FastPowOf2Matrix result,
                int start, int end) {
            this.a = a;
            this.b = b;
            this.result = result;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= 1) {
                for (int i = start; i < end; i++) {
                    for (int j = 0; j < a.getSize().el2(); j++) {
                        result.setElement(i + 1, j + 1, a.getElement(i + 1, j + 1) + b.getElement(i + 1, j + 1));
                    }
                }
            } else {
                int mid = (start + end) / 2;
                invokeAll(
                        new MAddTask(a, b, result, start, mid),
                        new MAddTask(a, b, result, mid, end));
            }
        }
    }

    private static class MSubTask extends RecursiveAction {
        private final FastPowOf2Matrix a, b, result;
        private final int start, end;

        public MSubTask(
                FastPowOf2Matrix a,
                FastPowOf2Matrix b,
                FastPowOf2Matrix result,
                int start, int end) {

            this.a = a;
            this.b = b;
            this.result = result;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= 1) {
                for (int i = start; i < end; i++) {
                    for (int j = 0; j < a.getSize().el2(); j++) {
                        result.setElement(i + 1, j + 1, a.getElement(i + 1, j + 1) - b.getElement(i + 1, j + 1));
                    }
                }
            } else {
                int mid = (start + end) / 2;
                invokeAll(
                        new MSubTask(a, b, result, start, mid),
                        new MSubTask(a, b, result, mid, end));
            }
        }
    }
}