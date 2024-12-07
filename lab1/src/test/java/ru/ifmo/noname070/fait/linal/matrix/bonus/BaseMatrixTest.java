package ru.ifmo.noname070.fait.linal.matrix.bonus;

import java.util.function.BooleanSupplier;

import ru.ifmo.noname070.fait.linal.testers.SpeedTester;
import ru.ifmo.noname070.fait.linal.testers.Tester;

public class BaseMatrixTest extends Tester {

    public static void main(String[] args) {
        test("BaseMatrix - get&set elements", BaseMatrixTest::testGetAndSetElement);
        test("BaseMatrix - additions", BaseMatrixTest::testAddition);
        test("BaseMatrix - mul", BaseMatrixTest::testMultiplication);
        test("BaseMatrix - mulScalar", BaseMatrixTest::testScalarMultiplication);
    }

    // заглушечка если надо делать таймтесты
    protected static void test(String name, BooleanSupplier testlink) {
        // Tester.test(name, testlink);
        SpeedTester.timeTest(name, testlink);
    }

    private static boolean testGetAndSetElement() {
        BaseMatrix matrix = new BaseMatrix(3, 3);
        matrix.setElement(1, 1, 5.0);
        matrix.setElement(2, 2, -3.0);
        matrix.setElement(2, 1, 4);

        return checkEquals(5.0, matrix.getElement(1, 1), "Ошибка в testGetAndSetElement: ожидаем 5.0") &&
               checkEquals(-3.0, matrix.getElement(2, 2), "Ошибка в testGetAndSetElement: ожидаем -3.0") &&
               checkEquals(4d, matrix.getElement(2, 1), "Ошибка в testGetAndSetElement: ожидаем -3.0");
    }

    private static boolean testAddition() {
        BaseMatrix matrix1 = new BaseMatrix(2, 2);
        BaseMatrix matrix2 = new BaseMatrix(2, 2);

        matrix1.setElement(1, 1, 3);
        matrix1.setElement(1, 2, 1.5);
        matrix2.setElement(1, 1, 2);
        matrix2.setElement(1, 2, 4);

        BaseMatrix result = (BaseMatrix) matrix1.add(matrix2);

        return checkEquals(5d, result.getElement(1, 1), "Ошибка в testAddition: элемент (1, 1)") &&
               checkEquals(5.5, result.getElement(1, 2), "Ошибка в testAddition: элемент (1, 2)") &&
               checkEquals(0d, result.getElement(2, 1), "Ошибка в testAddition: элемент (2, 1)") &&
               checkEquals(0d, result.getElement(2, 2), "Ошибка в testAddition: элемент (2, 2)");
    }

    private static boolean testMultiplication() {
        BaseMatrix matrix1 = new BaseMatrix(2, 2);
        BaseMatrix matrix2 = new BaseMatrix(2, 2);

        matrix1.setElement(1, 1, 1.0);
        matrix1.setElement(1, 2, 2.0);
        matrix1.setElement(2, 1, 3.0);
        matrix1.setElement(2, 2, 4.0);

        matrix2.setElement(1, 1, 2.0);
        matrix2.setElement(2, 1, 0.0);
        matrix2.setElement(1, 2, 1.0);
        matrix2.setElement(2, 2, 3.0);

        BaseMatrix result = (BaseMatrix) matrix1.mul(matrix2);

        return checkEquals(2.0, result.getElement(1, 1), "Ошибка в testMultiplication: элемент (1, 1)") &&
               checkEquals(7.0, result.getElement(1, 2), "Ошибка в testMultiplication: элемент (1, 2)") &&
               checkEquals(6.0, result.getElement(2, 1), "Ошибка в testMultiplication: элемент (2, 1)") &&
               checkEquals(15.0, result.getElement(2, 2), "Ошибка в testMultiplication: элемент (2, 2)");
    }

    private static boolean testScalarMultiplication() {
        BaseMatrix matrix = new BaseMatrix(2, 2);
        matrix.setElement(1, 1, 1.0);
        matrix.setElement(1, 2, -1.0);

        BaseMatrix result = (BaseMatrix) matrix.mulScalar(2.0);

        return checkEquals(2.0, result.getElement(1, 1), "Ошибка в testScalarMultiplication: элемент (1, 1)") &&
               checkEquals(-2.0, result.getElement(1, 2), "Ошибка в testScalarMultiplication: элемент (1, 2)");
    }
}
