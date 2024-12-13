package ru.ifmo.noname070.fait.linal;

import java.util.Objects;
import java.util.function.BooleanSupplier;

import ru.ifmo.noname070.fait.linal.testers.ITester;

public abstract class AbstractTest<T extends ITester> {
    protected final T tester;

    public AbstractTest(Class<T> testerClass) {
        try {
            this.tester = testerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void test(String name, BooleanSupplier testlink) {
        tester.test(name, testlink);
    }

    public boolean checkEquals(Object expected, Object actual, String errorMessage) {
        if (!Objects.equals(expected, actual)) {
            System.out.printf("%s : ожидалось [ %s ] получено [ %s ] ", errorMessage, expected, actual);
            return false;
        }
        return true;
    }
}