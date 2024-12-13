package ru.ifmo.noname070.fait.linal.matrix;

import ru.ifmo.noname070.fait.linal.AbstractTest;
import ru.ifmo.noname070.fait.linal.testers.ITester;

public class SparseMatrixTest<T extends ITester> extends AbstractTest<T> {
    SparseMatrix matrix1;
    SparseMatrix matrix2;

    public SparseMatrixTest(Class<T> testerClass) {
        super(testerClass);
    }

    public void run() {
        test("SparseMatrix - get&set elements", this::testGetAndSetElement);
        test("SparseMatrix - additions", this::testAddition);
        test("SparseMatrix - mul", this::testMultiplication);
        test("SparseMatrix - mulScalar", this::testScalarMultiplication);
        test("SparseMatrix - mulScalar 2", this::testScalarMultiplication2);
        test("SparseMatrix - trace", this::testTraceCalculation);
        test("SparseMatrix - det", this::testDeterminant);
        test("SparseMatrix - hasInverse", this::testHasInverse);

        genMatrix(128);
        test("SparseMatrix - stress test mul (128) ", this::testStressTestMultiplication);

        genMatrix(256);
        test("SparseMatrix - stress test mul (256) ", this::testStressTestMultiplication);

        genMatrix(512);
        test("SparseMatrix - stress test mul (512) ", this::testStressTestMultiplication);

        genMatrix(1024);
        test("SparseMatrix - stress test mul (1024) ", this::testStressTestMultiplication);
    }

    private void genMatrix(int n) {
        matrix1 = new SparseMatrix(n, n);
        matrix2 = new SparseMatrix(n, n);

        for (int i = 1; i <= matrix1.getSize().el1(); i++) {
            for (int j = 1; j <= matrix2.getSize().el1(); j++) {
                matrix1.setElement(i, j, Math.random() * 1000);
                matrix2.setElement(i, j, Math.random() * 1000);
            }
        }
    }

    private boolean testGetAndSetElement() {
        SparseMatrix matrix = new SparseMatrix(3, 3);
        matrix.setElement(1, 1, 5.0);
        matrix.setElement(2, 2, -3.0);

        return checkEquals(5.0, matrix.getElement(1, 1), "Ошибка в testGetAndSetElement: ожидаем 5.0");
    }

    private boolean testAddition() {
        SparseMatrix matrix1 = new SparseMatrix(2, 2);
        SparseMatrix matrix2 = new SparseMatrix(2, 2);

        matrix1.setElement(1, 1, 3.0);
        matrix1.setElement(2, 2, 1.5);
        matrix2.setElement(1, 1, 2.0);
        matrix2.setElement(1, 2, 4.0);

        SparseMatrix result = (SparseMatrix) matrix1.add(matrix2);

        return checkEquals(5.0, result.getElement(1, 1), "Ошибка в testAddition: элемент (1, 1)");
    }

    private boolean testMultiplication() {
        SparseMatrix matrix1 = new SparseMatrix(2, 2);
        SparseMatrix matrix2 = new SparseMatrix(2, 2);

        matrix1.setElement(1, 1, 1.0);
        matrix1.setElement(1, 2, 2.0);
        matrix1.setElement(2, 1, 3.0);
        matrix1.setElement(2, 2, 4.0);

        matrix2.setElement(1, 1, 5.0);
        matrix2.setElement(1, 2, 6.0);
        matrix2.setElement(2, 1, 0.0);
        matrix2.setElement(2, 2, 1.0);

        SparseMatrix result = (SparseMatrix) matrix1.mul(matrix2);

        return checkEquals(5d, result.getElement(1, 1), "Ошибка в testMultiplication: элемент (1, 1)");
    }

    private boolean testScalarMultiplication2() {
        SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setElement(1, 1, 3.0);
        matrix.setElement(1, 2, -1.5);
        matrix.setElement(2, 1, 2.0);
        matrix.setElement(2, 2, 4.0);

        SparseMatrix result = (SparseMatrix) matrix.mulScalar(2.0);

        return checkEquals(6.0, result.getElement(1, 1), "Ошибка в testScalarMultiplication: элемент (1, 1)") &&
                checkEquals(-3.0, result.getElement(1, 2), "Ошибка в testScalarMultiplication: элемент (1, 2)") &&
                checkEquals(4.0, result.getElement(2, 1), "Ошибка в testScalarMultiplication: элемент (2, 1)") &&
                checkEquals(8.0, result.getElement(2, 2), "Ошибка в testScalarMultiplication: элемент (2, 2)");
    }

    private boolean testScalarMultiplication() {
        SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setElement(1, 1, 1.0);
        matrix.setElement(1, 2, -1.0);

        SparseMatrix result = (SparseMatrix) matrix.mulScalar(2.0);

        return checkEquals(2.0, result.getElement(1, 1), "Ошибка в testScalarMultiplication: элемент (1, 1)") &&
                checkEquals(-2.0, result.getElement(1, 2), "Ошибка в testScalarMultiplication: элемент (1, 2)");
    }

    private boolean testTraceCalculation() {
        SparseMatrix matrix = new SparseMatrix(3, 3);
        matrix.setElement(1, 1, 1.0);
        matrix.setElement(2, 2, 2.0);
        matrix.setElement(3, 3, 3.0);

        return checkEquals(6.0, matrix.calculateTrace(), "Ошибка в testTraceCalculation");
    }

    private boolean testDeterminant() {
        SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setElement(1, 1, 1.0);
        matrix.setElement(1, 2, 2.0);
        matrix.setElement(2, 1, 3.0);
        matrix.setElement(2, 2, 4.0);

        return checkEquals(-2.0, matrix.det(), "Ошибка в testDeterminant");
    }

    private boolean testHasInverse() {
        SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setElement(1, 1, 1.0);
        matrix.setElement(1, 2, 2.0);
        matrix.setElement(2, 1, 3.0);
        matrix.setElement(2, 2, 4.0);

        return checkEquals(true, matrix.hasInverse(), "Ошибка в testHasInverse");
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