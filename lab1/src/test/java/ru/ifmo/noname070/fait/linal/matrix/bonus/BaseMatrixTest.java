package ru.ifmo.noname070.fait.linal.matrix.bonus;

import ru.ifmo.noname070.fait.linal.AbstractTest;
import ru.ifmo.noname070.fait.linal.matrix.IMatrix;
import ru.ifmo.noname070.fait.linal.testers.ITester;

public class BaseMatrixTest<T extends ITester> extends AbstractTest<T> {
    BaseMatrix matrix1;
    BaseMatrix matrix2;

    public BaseMatrixTest(Class<T> testerClass) {
        super(testerClass);
    }

    public void run() {
        test("BaseMatrix - get&set elements", this::testGetAndSetElement);
        test("BaseMatrix - additions", this::testAddition);
        test("BaseMatrix - mul", this::testMultiplication);
        test("BaseMatrix - mulScalar", this::testScalarMultiplication);

        genMatrix(128);
        test("BaseMatrix - stress test mul (128)", this::testStressTestMultiplication);

        genMatrix(256);
        test("BaseMatrix - stress test mul (256)", this::testStressTestMultiplication);

        genMatrix(512);
        test("BaseMatrix - stress test mul (512)", this::testStressTestMultiplication);

        genMatrix(1024);
        test("BaseMatrix - stress test mul (1024)", this::testStressTestMultiplication);
    }

    private void genMatrix(int n) {
        matrix1 = new BaseMatrix(n, n);
        matrix2 = new BaseMatrix(n, n);

        for (int i = 1; i <= matrix1.getSize().el1(); i++) {
            for (int j = 1; j <= matrix2.getSize().el1(); j++) {
                matrix1.setElement(i, j, Math.random() * 1000);
                matrix2.setElement(i, j, Math.random() * 1000);
            }
        }
    }

    private boolean testGetAndSetElement() {
        BaseMatrix matrix = new BaseMatrix(3, 3);
        matrix.setElement(1, 1, 5.0);
        matrix.setElement(2, 2, -3.0);
        matrix.setElement(2, 1, 4);

        return checkEquals(5.0, matrix.getElement(1, 1), "Ошибка в testGetAndSetElement: ожидаем 5.0") &&
                checkEquals(-3.0, matrix.getElement(2, 2), "Ошибка в testGetAndSetElement: ожидаем -3.0") &&
                checkEquals(4d, matrix.getElement(2, 1), "Ошибка в testGetAndSetElement: ожидаем -3.0");
    }

    private boolean testAddition() {
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

    private boolean testMultiplication() {
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

    private boolean testScalarMultiplication() {
        BaseMatrix matrix = new BaseMatrix(2, 2);
        matrix.setElement(1, 1, 1.0);
        matrix.setElement(1, 2, -1.0);

        BaseMatrix result = (BaseMatrix) matrix.mulScalar(2.0);

        return checkEquals(2.0, result.getElement(1, 1), "Ошибка в testScalarMultiplication: элемент (1, 1)") &&
                checkEquals(-2.0, result.getElement(1, 2), "Ошибка в testScalarMultiplication: элемент (1, 2)");
    }

    private boolean testStressTestMultiplication() {
        try {

            IMatrix result = (IMatrix) matrix1.mul(matrix2);

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
