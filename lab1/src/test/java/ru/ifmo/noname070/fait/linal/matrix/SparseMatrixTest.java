package ru.ifmo.noname070.fait.linal.matrix;

import java.util.function.BooleanSupplier;

import ru.ifmo.noname070.fait.linal.testers.SpeedTester;
import ru.ifmo.noname070.fait.linal.testers.Tester;

public class SparseMatrixTest extends Tester {

    public static void main(String[] args) {
        test("SparseMatrix - get&set elements", SparseMatrixTest::testGetAndSetElement);
        test("SparseMatrix - additions", SparseMatrixTest::testAddition);
        test("SparseMatrix - mul", SparseMatrixTest::testMultiplication);
        test("SparseMatrix - mulScalar", SparseMatrixTest::testScalarMultiplication);
        test("SparseMatrix - trace", SparseMatrixTest::testTraceCalculation);
        test("SparseMatrix - det", SparseMatrixTest::testDeterminant);
        test("SparseMatrix - hasInverse", SparseMatrixTest::testHasInverse);
    }

    // заглушечка если надо делать таймтесты
    protected static void test(String name, BooleanSupplier testlink) {
        SpeedTester.timeTest(name, testlink);
        // Tester.test(name, testlink);
    }

    protected static boolean testGetAndSetElement() {
        SparseMatrix matrix = new SparseMatrix(3, 3);
        matrix.setElement(1, 1, 5.0);
        matrix.setElement(2, 2, -3.0);

        return checkEquals(5.0, matrix.getElement(1, 1), "Ошибка в testGetAndSetElement: ожидаем 5.0");
    }

    private static boolean testAddition() {
        SparseMatrix matrix1 = new SparseMatrix(2, 2);
        SparseMatrix matrix2 = new SparseMatrix(2, 2);

        matrix1.setElement(1, 1, 3.0);
        matrix1.setElement(2, 2, 1.5);
        matrix2.setElement(1, 1, 2.0);
        matrix2.setElement(1, 2, 4.0);

        SparseMatrix result = (SparseMatrix) matrix1.add(matrix2);

        return checkEquals(5.0, result.getElement(1, 1), "Ошибка в testAddition: элемент (1, 1)");
    }

    private static boolean testMultiplication() {
        SparseMatrix matrix1 = new SparseMatrix(2, 2);
        SparseMatrix matrix2 = new SparseMatrix(2, 2);

        matrix1.setElement(1, 1, 1.0);
        matrix1.setElement(1, 2, 2.0);
        matrix1.setElement(2, 1, 3.0);
        matrix1.setElement(2, 2, 4.0);

        matrix2.setElement(1, 1, 2.0);
        matrix2.setElement(2, 1, 0.0);
        matrix2.setElement(1, 2, 1.0);
        matrix2.setElement(2, 2, 3.0);

        SparseMatrix result = (SparseMatrix) matrix1.mul(matrix2);

        return checkEquals(2.0, result.getElement(1, 1), "Ошибка в testMultiplication: элемент (1, 1)");
    }

    private static boolean testScalarMultiplication() {
        SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setElement(1, 1, 1.0);
        matrix.setElement(1, 2, -1.0);

        SparseMatrix result = (SparseMatrix) matrix.mulScalar(2.0);

        return checkEquals(2.0, result.getElement(1, 1), "Ошибка в testScalarMultiplication: элемент (1, 1)") &&
                checkEquals(-2.0, result.getElement(1, 2), "Ошибка в testScalarMultiplication: элемент (1, 2)");
    }

    private static boolean testTraceCalculation() {
        SparseMatrix matrix = new SparseMatrix(3, 3);
        matrix.setElement(1, 1, 1.0);
        matrix.setElement(2, 2, 2.0);
        matrix.setElement(3, 3, 3.0);

        return checkEquals(6.0, matrix.calculateTrace(), "Ошибка в testTraceCalculation");
    }

    private static boolean testDeterminant() {
        SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setElement(1, 1, 1.0);
        matrix.setElement(1, 2, 2.0);
        matrix.setElement(2, 1, 3.0);
        matrix.setElement(2, 2, 4.0);

        return checkEquals(-2.0, matrix.det(), "Ошибка в testDeterminant");
    }

    private static boolean testHasInverse() {
        SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setElement(1, 1, 1.0);
        matrix.setElement(1, 2, 2.0);
        matrix.setElement(2, 1, 3.0);
        matrix.setElement(2, 2, 4.0);

        return checkEquals(true, matrix.hasInverse(), "Ошибка в testHasInverse");
    }

}