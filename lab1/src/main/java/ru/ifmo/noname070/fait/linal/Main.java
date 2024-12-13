package ru.ifmo.noname070.fait.linal;

import ru.ifmo.noname070.fait.linal.matrix.IMatrix;
import ru.ifmo.noname070.fait.linal.matrix.SparseMatrix;
import ru.ifmo.noname070.fait.linal.utils.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {

    public void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Введите размер матрицы (n m) >");
            int[] dims = readIntArray(reader, 2);
            SparseMatrix matrix = new SparseMatrix(new Pair<>(dims[0], dims[1]));

            System.out.println("Введите элементы матрицы построчно:");
            readMatrix(reader, matrix);

            System.out.println("След матрицы: " + matrix.calculateTrace());

            System.out.print("Введите индекс элемента (row column) > ");
            int[] idxs = readIntArray(reader, 2);
            System.out.printf("Элемент (%d, %d): %s%n",
                    idxs[0], idxs[1],
                    matrix.getElement(idxs[0], idxs[1]));

            System.out.println("Создадим вторую матрицу такого же размера для операций:");
            SparseMatrix matrix2 = new SparseMatrix(matrix.getSize());
            readMatrix(reader, matrix2);

            matrix = (SparseMatrix) matrix.add(matrix2);
            System.out.println("Результат сложения матриц:\n" + matrix);

            System.out.print("Введите скаляр для умножения > ");
            double scalar = Double.parseDouble(reader.readLine());
            ;
            System.out.println("Результат умножения на скаляр:\n" + matrix.mulScalar(scalar));

            if (matrix.isSquare()) {
                System.out.println("Определитель матрицы: " + matrix.det());
                System.out.println("Матрица обратима? " + (matrix.hasInverse() ? "да" : "нет"));
            } else {
                System.out.println("Определитель и обратимость применимы только к квадратным матрицам.");
            }

        } catch (Exception e) {
            System.err.println("Произошла ошибочка:" + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void readMatrix(BufferedReader reader, IMatrix matrix) {
        try {
            for (int i = 1; i <= matrix.getSize().el1(); i++) {
                System.out.printf("Строка %d >", i);
                double[] row = readDoubleArray(reader, matrix.getSize().el2());
                for (int j = 1; j <= row.length; j++) {
                    matrix.setElement(i, j, row[j - 1]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении матрицы: " + e.getLocalizedMessage(), e);
        }
    }

    private int[] readIntArray(BufferedReader reader, int size) throws Exception {
        return Arrays.stream(
                reader.readLine()
                        .split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private double[] readDoubleArray(BufferedReader reader, int size) throws Exception {
        return Arrays.stream(
                reader.readLine()
                        .split("\\s+"))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }
}
