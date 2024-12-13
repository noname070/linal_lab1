package ru.ifmo.noname070.fait.linal.matrix.bonus;

import ru.ifmo.noname070.fait.linal.AbstractTest;
import ru.ifmo.noname070.fait.linal.matrix.IMatrix;
import ru.ifmo.noname070.fait.linal.testers.ITester;

public class FastPowOf2MatrixTest<T extends ITester> extends AbstractTest<T> {

    public FastPowOf2MatrixTest(Class<T> testerClass) {
        super(testerClass);
    }

    private FastPowOf2Matrix matrix1;
    private FastPowOf2Matrix matrix2;

    public void run() {
        test("FastPowOf2Matrix - constructor valid", this::testConstructorValid);
        test("FastPowOf2Matrix - constructor invalid", this::testConstructorInvalid);
        test("FastPowOf2Matrix - mul valid", this::testMultiplicationValid);
        test("FastPowOf2Matrix - mul invalid", this::testMultiplicationInvalid);
        test("FastPowOf2Matrix - add valid", this::testAddValid);
        test("FastPowOf2Matrix - add invalid", this::testAddInvalid);
        test("FastPowOf2Matrix - determinant 2x2", this::testDeterminant2x2);
        test("FastPowOf2Matrix - determinant 4x4", this::testDeterminant4x4);
        test("FastPowOf2Matrix - determinant 8x8 matrix", this::testDeterminant8x8);

        genMatrix(128);
        test("FastPowOf2Matrix - stress test mul (128)", this::testStressTestMultiplication);

        genMatrix(256);
        test("FastPowOf2Matrix - stress test mul (256)", this::testStressTestMultiplication);

        genMatrix(512);
        test("FastPowOf2Matrix - stress test mul (512)", this::testStressTestMultiplication);

        genMatrix(1024);
        test("FastPowOf2Matrix - stress test mul (1024)", this::testStressTestMultiplication);
    }

    private void genMatrix(int n) {
        matrix1 = new FastPowOf2Matrix(n, n);
        matrix2 = new FastPowOf2Matrix(n, n);

        for (int i = 1; i <= matrix1.getSize().el1(); i++) {
            for (int j = 1; j <= matrix2.getSize().el1(); j++) {
                matrix1.setElement(i, j, Math.random() * 1000);
                matrix2.setElement(i, j, Math.random() * 1000);
            }
        }
    }

    private boolean testConstructorValid() {
        try {
            FastPowOf2Matrix matrix = new FastPowOf2Matrix(4, 4);
            return checkEquals(4, matrix.getSize().el1(), "Ошибка в testConstructorValid: ожидаем размерность 4x4");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean testConstructorInvalid() {
        try {
            new FastPowOf2Matrix(3, 3);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private boolean testMultiplicationValid() {
        FastPowOf2Matrix matrix1 = new FastPowOf2Matrix(2, 2);
        matrix1.setElement(0, 0, 1);
        matrix1.setElement(0, 1, 2);
        matrix1.setElement(1, 0, 3);
        matrix1.setElement(1, 1, 4);

        FastPowOf2Matrix matrix2 = new FastPowOf2Matrix(2, 2);
        matrix2.setElement(0, 0, 5);
        matrix2.setElement(0, 1, 6);
        matrix2.setElement(1, 0, 7);
        matrix2.setElement(1, 1, 8);

        FastPowOf2Matrix result = (FastPowOf2Matrix) matrix1.mul(matrix2);

        return checkEquals(19d, result.getElement(0, 0), "Ошибка в testMultiplicationValid: неверный элемент (1,1)") &&
                checkEquals(22d, result.getElement(0, 1), "Ошибка в testMultiplicationValid: неверный элемент (1,2)") &&
                checkEquals(43d, result.getElement(1, 0), "Ошибка в testMultiplicationValid: неверный элемент (2,1)") &&
                checkEquals(50d, result.getElement(1, 1), "Ошибка в testMultiplicationValid: неверный элемент (2,2)");
    }

    private boolean testMultiplicationInvalid() {
        try {
            FastPowOf2Matrix matrix1 = new FastPowOf2Matrix(2, 2);
            FastPowOf2Matrix matrix2 = new FastPowOf2Matrix(3, 3);
            matrix1.mul(matrix2);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private boolean testAddValid() {
        IFastMatrix matrix1 = new FastPowOf2Matrix(2, 2);
        matrix1.setElement(0, 0, 1);
        matrix1.setElement(0, 1, 2);
        matrix1.setElement(1, 0, 3);
        matrix1.setElement(1, 1, 4);

        IFastMatrix matrix2 = new FastPowOf2Matrix(2, 2);
        matrix2.setElement(0, 0, 5);
        matrix2.setElement(0, 1, 6);
        matrix2.setElement(1, 0, 7);
        matrix2.setElement(1, 1, 8);

        IMatrix result = matrix1.add(matrix2);

        return checkEquals(6d, result.getElement(0, 0), "Ошибка в testAddValid: неверный элемент (1,1)") &&
                checkEquals(8d, result.getElement(0, 1), "Ошибка в testAddValid: неверный элемент (1,2)") &&
                checkEquals(10d, result.getElement(1, 0), "Ошибка в testAddValid: неверный элемент (2,1)") &&
                checkEquals(12d, result.getElement(1, 1), "Ошибка в testAddValid: неверный элемент (2,2)");
    }

    private boolean testAddInvalid() {
        try {
            FastPowOf2Matrix matrix1 = new FastPowOf2Matrix(2, 2);
            FastPowOf2Matrix matrix2 = new FastPowOf2Matrix(3, 3);
            matrix1.add(matrix2);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private boolean testDeterminant2x2() {
        FastPowOf2Matrix matrix = new FastPowOf2Matrix(2, 2);
        matrix.setElement(1, 1, 1);
        matrix.setElement(1, 2, 2);
        matrix.setElement(2, 1, 3);
        matrix.setElement(2, 2, 4);

        return checkEquals(-2d, matrix.det(), "Ошибка в testDeterminant2x2: неверный детерминант");
    }

    private boolean testDeterminant4x4() {
        FastPowOf2Matrix matrix = new FastPowOf2Matrix(4, 4);
        matrix.setElement(0, 0, 1);
        matrix.setElement(0, 1, 2);
        matrix.setElement(0, 2, 3);
        matrix.setElement(0, 3, 4);
        matrix.setElement(1, 0, 5);
        matrix.setElement(1, 1, 6);
        matrix.setElement(1, 2, 7);
        matrix.setElement(1, 3, 8);
        matrix.setElement(2, 0, 9);
        matrix.setElement(2, 1, 10);
        matrix.setElement(2, 2, 11);
        matrix.setElement(2, 3, 12);
        matrix.setElement(3, 0, 13);
        matrix.setElement(3, 1, 14);
        matrix.setElement(3, 2, 15);
        matrix.setElement(3, 3, 16);

        return checkEquals(0d, matrix.det(), "Ошибка в testDeterminant4x4: неверный детерминант");
    }

    private boolean testDeterminant8x8() {
        FastPowOf2Matrix matrix = new FastPowOf2Matrix(8, 8);
        for (int i = 0; i <= 8; i++) {
            for (int j = 0; j <= 8; j++) {
                matrix.setElement(i, j, Math.random() * 10);
            }
        }

        double determinant = matrix.det();
        return !Double.isNaN(determinant) && !Double.isInfinite(determinant);
    }

    private boolean testStressTestMultiplication() {
        try {

            FastPowOf2Matrix result = (FastPowOf2Matrix) matrix1.mul(matrix2);

            if (!result.getSize().el1().equals(matrix1.getSize().el1())
                    || !result.getSize().el2().equals(matrix2.getSize().el2())) {
                return checkEquals(true, false,
                        "Ошибка в testStressTestMultiplication: размер результата некорректен : %s x %s | %s x %s"
                                .formatted(
                                        result.getSize().el1(), result.getSize().el2(),
                                        matrix1.getSize().el1(), matrix1.getSize().el2()));
            }

            for (int i = 1; i <= matrix1.getSize().el1(); i++) {
                for (int j = 1; j <= matrix2.getSize().el1(); j++) {
                    double value = result.getElement(i, j);
                    if (Double.isNaN(value) || Double.isInfinite(value)) {
                        return checkEquals(true, false,
                                "Ошибка в testStressTestMultiplication: найден некорректный элемент");
                    }
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return checkEquals(true, false, "Ошибка в testStressTestMultiplication: исключение " + e.getMessage());
        }
    }

}