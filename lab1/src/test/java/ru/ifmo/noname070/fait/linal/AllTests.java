package ru.ifmo.noname070.fait.linal;

import ru.ifmo.noname070.fait.linal.matrix.*;
import ru.ifmo.noname070.fait.linal.matrix.bonus.*;
import ru.ifmo.noname070.fait.linal.testers.*;

/*
 * ~50% тестов сгенерировано (мне лень писать (на самом деле - больше xd))
 * микрофрейм для тестов писал сам :умни
 */
public class AllTests {

    public static void main(String[] args) {
        new BaseMatrixTest<>(SpeedTester.class).run();
        new SparseMatrixTest<>(SpeedTester.class).run();
        new FastPowOf2MatrixTest<>(SpeedTester.class).run();
    }
}


/*
Тест: BaseMatrix - get&set elements                     прошел успешно    Время выполнения: 0,00404 сек
Тест: BaseMatrix - additions                            прошел успешно    Время выполнения: 0,01279 сек
Тест: BaseMatrix - mul                                  прошел успешно    Время выполнения: 0,00021 сек
Тест: BaseMatrix - mulScalar                            прошел успешно    Время выполнения: 0,00180 сек
Тест: BaseMatrix - stress test mul (128)                прошел успешно    Время выполнения: 0,01501 сек
Тест: BaseMatrix - stress test mul (256)                прошел успешно    Время выполнения: 0,02220 сек
Тест: BaseMatrix - stress test mul (512)                прошел успешно    Время выполнения: 0,12360 сек
Тест: BaseMatrix - stress test mul (1024)               прошел успешно    Время выполнения: 1,54465 сек
Тест: SparseMatrix - get&set elements                   прошел успешно    Время выполнения: 0,00173 сек
Тест: SparseMatrix - additions                          прошел успешно    Время выполнения: 0,00101 сек
Тест: SparseMatrix - mul                                прошел успешно    Время выполнения: 0,00024 сек
Тест: SparseMatrix - mulScalar                          прошел успешно    Время выполнения: 0,00115 сек
Тест: SparseMatrix - mulScalar 2                        прошел успешно    Время выполнения: 0,00030 сек
Тест: SparseMatrix - trace                              прошел успешно    Время выполнения: 0,00225 сек
Тест: SparseMatrix - det                                прошел успешно    Время выполнения: 0,00017 сек
Тест: SparseMatrix - hasInverse                         прошел успешно    Время выполнения: 0,00027 сек
Тест: SparseMatrix - stress test mul (128)              прошел успешно    Время выполнения: 0,07695 сек
Тест: SparseMatrix - stress test mul (256)              прошел успешно    Время выполнения: 0,61248 сек
Тест: SparseMatrix - stress test mul (512)              прошел успешно    Время выполнения: 4,81006 сек
Тест: SparseMatrix - stress test mul (1024)             прошел успешно    Время выполнения: 49,33398 сек
Тест: FastPowOf2Matrix - constructor valid              прошел успешно    Время выполнения: 0,00061 сек
Тест: FastPowOf2Matrix - constructor invalid            прошел успешно    Время выполнения: 0,00016 сек
Тест: FastPowOf2Matrix - mul valid                      прошел успешно    Время выполнения: 0,00367 сек
Тест: FastPowOf2Matrix - mul invalid                    прошел успешно    Время выполнения: 0,00013 сек
Тест: FastPowOf2Matrix - add valid                      прошел успешно    Время выполнения: 0,00090 сек
Тест: FastPowOf2Matrix - add invalid                    прошел успешно    Время выполнения: 0,00011 сек
Тест: FastPowOf2Matrix - determinant 2x2                прошел успешно    Время выполнения: 0,00027 сек
Тест: FastPowOf2Matrix - determinant 4x4                прошел успешно    Время выполнения: 0,00027 сек
Тест: FastPowOf2Matrix - determinant 8x8 matrix         прошел успешно    Время выполнения: 0,00948 сек
Тест: FastPowOf2Matrix - stress test mul (128)          прошел успешно    Время выполнения: 0,12634 сек
Тест: FastPowOf2Matrix - stress test mul (256)          прошел успешно    Время выполнения: 0,54964 сек
Тест: FastPowOf2Matrix - stress test mul (512)          прошел успешно    Время выполнения: 9,84967 сек
Тест: FastPowOf2Matrix - stress test mul (1024)         прошел успешно    Время выполнения: 87,14972 сек
*/