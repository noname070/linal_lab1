package ru.ifmo.noname070.fait.linal.matrix.bonus;

import java.util.function.BooleanSupplier;

import ru.ifmo.noname070.fait.linal.matrix.IMatrix;
import ru.ifmo.noname070.fait.linal.testers.SpeedTester;
import ru.ifmo.noname070.fait.linal.testers.Tester;

public class FastPowOf2MatrixTest extends Tester {

    private static FastPowOf2Matrix matrix1;
    private static FastPowOf2Matrix matrix2;

    public static void main(String[] args) {
        test("FastPowOf2Matrix - constructor valid", FastPowOf2MatrixTest::testConstructorValid);
        test("FastPowOf2Matrix - constructor invalid", FastPowOf2MatrixTest::testConstructorInvalid);
        test("FastPowOf2Matrix - mul valid", FastPowOf2MatrixTest::testMultiplicationValid);
        test("FastPowOf2Matrix - mul invalid", FastPowOf2MatrixTest::testMultiplicationInvalid);
        test("FastPowOf2Matrix - add valid", FastPowOf2MatrixTest::testAddValid);
        test("FastPowOf2Matrix - add invalid", FastPowOf2MatrixTest::testAddInvalid);
        test("FastPowOf2Matrix - determinant 2x2", FastPowOf2MatrixTest::testDeterminant2x2);
        test("FastPowOf2Matrix - determinant 4x4", FastPowOf2MatrixTest::testDeterminant4x4);
        test("FastPowOf2Matrix - determinant large matrix", FastPowOf2MatrixTest::testDeterminantLargeMatrix);

        genMatrix(128);
        test("FastPowOf2Matrix - stress test mul (128)", FastPowOf2MatrixTest::testStressTestMultiplication);

        genMatrix(256);
        test("FastPowOf2Matrix - stress test mul (256)", FastPowOf2MatrixTest::testStressTestMultiplication);
        // ==== DANGER ZONE ====
        // genMatrix(512);
        // test("FastPowOf2Matrix - stress test mul (512)", FastPowOf2MatrixTest::testStressTestMultiplication);

        // genMatrix(1024);
        // test("FastPowOf2Matrix - stress test mul (1024)", FastPowOf2MatrixTest::testStressTestMultiplication);
    }


    private static void genMatrix(int n) {
        matrix1 = new FastPowOf2Matrix(n, n);
        matrix2 = new FastPowOf2Matrix(n, n);
    
        for (int i = 1; i <= matrix1.getSize().el1(); i++) {
            for (int j = 1; j <= matrix2.getSize().el1(); j++) {
                matrix1.setElement(i, j, Math.random() * 1000);
                matrix2.setElement(i, j, Math.random() * 1000);
            }
        }
    }

    protected static void test(String name, BooleanSupplier testlink) {
        SpeedTester.timeTest(name, testlink);
    }

    private static boolean testConstructorValid() {
        try {
            FastPowOf2Matrix matrix = new FastPowOf2Matrix(4, 4);
            return checkEquals(4, matrix.getSize().el1(), "Ошибка в testConstructorValid: ожидаем размерность 4x4");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean testConstructorInvalid() {
        try {
            new FastPowOf2Matrix(3, 3);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static boolean testMultiplicationValid() {
        FastPowOf2Matrix matrix1 = new FastPowOf2Matrix(2, 2);
        matrix1.setElement(1, 1, 1);
        matrix1.setElement(1, 2, 2);
        matrix1.setElement(2, 1, 3);
        matrix1.setElement(2, 2, 4);

        FastPowOf2Matrix matrix2 = new FastPowOf2Matrix(2, 2);
        matrix2.setElement(1, 1, 5);
        matrix2.setElement(1, 2, 6);
        matrix2.setElement(2, 1, 7);
        matrix2.setElement(2, 2, 8);

        FastPowOf2Matrix result = (FastPowOf2Matrix) matrix1.mul(matrix2);

        return checkEquals(19d, result.getElement(1, 1), "Ошибка в testMultiplicationValid: неверный элемент (1,1)") &&
                checkEquals(22d, result.getElement(1, 2), "Ошибка в testMultiplicationValid: неверный элемент (1,2)") &&
                checkEquals(43d, result.getElement(2, 1), "Ошибка в testMultiplicationValid: неверный элемент (2,1)") &&
                checkEquals(50d, result.getElement(2, 2), "Ошибка в testMultiplicationValid: неверный элемент (2,2)");
    }

    private static boolean testMultiplicationInvalid() {
        try {
            FastPowOf2Matrix matrix1 = new FastPowOf2Matrix(2, 2);
            FastPowOf2Matrix matrix2 = new FastPowOf2Matrix(3, 3);
            matrix1.mul(matrix2);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static boolean testAddValid() {
        FastPowOf2Matrix matrix1 = new FastPowOf2Matrix(2, 2);
        matrix1.setElement(1, 1, 1);
        matrix1.setElement(1, 2, 2);
        matrix1.setElement(2, 1, 3);
        matrix1.setElement(2, 2, 4);

        FastPowOf2Matrix matrix2 = new FastPowOf2Matrix(2, 2);
        matrix2.setElement(1, 1, 5);
        matrix2.setElement(1, 2, 6);
        matrix2.setElement(2, 1, 7);
        matrix2.setElement(2, 2, 8);

        IMatrix result = matrix1.add(matrix2);

        return checkEquals(6d, result.getElement(1, 1), "Ошибка в testAddValid: неверный элемент (1,1)") &&
                checkEquals(8d, result.getElement(1, 2), "Ошибка в testAddValid: неверный элемент (1,2)") &&
                checkEquals(10d, result.getElement(2, 1), "Ошибка в testAddValid: неверный элемент (2,1)") &&
                checkEquals(12d, result.getElement(2, 2), "Ошибка в testAddValid: неверный элемент (2,2)");
    }

    private static boolean testAddInvalid() {
        try {
            FastPowOf2Matrix matrix1 = new FastPowOf2Matrix(2, 2);
            FastPowOf2Matrix matrix2 = new FastPowOf2Matrix(3, 3);
            matrix1.add(matrix2);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static boolean testDeterminant2x2() {
        FastPowOf2Matrix matrix = new FastPowOf2Matrix(2, 2);
        matrix.setElement(1, 1, 1);
        matrix.setElement(1, 2, 2);
        matrix.setElement(2, 1, 3);
        matrix.setElement(2, 2, 4);

        return checkEquals(-2d, matrix.det(), "Ошибка в testDeterminant2x2: неверный детерминант");
    }

    private static boolean testDeterminant4x4() {
        FastPowOf2Matrix matrix = new FastPowOf2Matrix(4, 4);
        matrix.setElement(1, 1, 1);
        matrix.setElement(1, 2, 2);
        matrix.setElement(1, 3, 3);
        matrix.setElement(1, 4, 4);
        matrix.setElement(2, 1, 5);
        matrix.setElement(2, 2, 6);
        matrix.setElement(2, 3, 7);
        matrix.setElement(2, 4, 8);
        matrix.setElement(3, 1, 9);
        matrix.setElement(3, 2, 10);
        matrix.setElement(3, 3, 11);
        matrix.setElement(3, 4, 12);
        matrix.setElement(4, 1, 13);
        matrix.setElement(4, 2, 14);
        matrix.setElement(4, 3, 15);
        matrix.setElement(4, 4, 16);

        return checkEquals(0d, matrix.det(), "Ошибка в testDeterminant4x4: неверный детерминант");
    }

    private static boolean testDeterminantLargeMatrix() {
        FastPowOf2Matrix matrix = new FastPowOf2Matrix(8, 8);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                matrix.setElement(i, j, Math.random() * 10);
            }
        }

        double determinant = matrix.det();
        return !Double.isNaN(determinant) && !Double.isInfinite(determinant);
    }

    private static boolean testStressTestMultiplication() {
        try {

            FastPowOf2Matrix result = (FastPowOf2Matrix) matrix1.mul(matrix2);
    
            if (!result.getSize().el1().equals(matrix1.getSize().el1()) || !result.getSize().el2().equals(matrix2.getSize().el2())) {
                return checkEquals(true, false, "Ошибка в testStressTestMultiplication: размер результата некорректен : %s x %s | %s x %s".formatted(
                    result.getSize().el1(), result.getSize().el2(),
                    matrix1.getSize().el1(), matrix1.getSize().el2()
                ));
            }
    
            for (int i = 1; i <= matrix1.getSize().el1(); i++) {
                for (int j = 1; j <= matrix2.getSize().el1(); j++) {
                    double value = result.getElement(i, j);
                    if (Double.isNaN(value) || Double.isInfinite(value)) {
                        return checkEquals(true, false, "Ошибка в testStressTestMultiplication: найден некорректный элемент");
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