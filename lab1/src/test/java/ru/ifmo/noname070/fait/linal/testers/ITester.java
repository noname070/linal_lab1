package ru.ifmo.noname070.fait.linal.testers;

import java.util.function.BooleanSupplier;

public interface ITester {
    void test(String name, BooleanSupplier testlink);
}
