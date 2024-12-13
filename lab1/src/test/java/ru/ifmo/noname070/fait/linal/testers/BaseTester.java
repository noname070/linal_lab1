package ru.ifmo.noname070.fait.linal.testers;

import java.util.function.BooleanSupplier;

public class BaseTester implements ITester {

    public BaseTester() {
    }

    @Override
    public void test(String name, BooleanSupplier testlink) {
        try {
            System.out.printf("Тест: %-50s", name);
            if (testlink.getAsBoolean()) {
                System.out.print("\u001B[32m" + "прошел успешно" + "\u001B[0m\n");
            } else {
                System.out.print("\u001B[31m" + "не прошел" + "\u001B[0m\n");
            }
        } catch (Exception e) {
            System.out.printf("%-30s : \u001B[33mвызвал исключение: %s\u001B[0m\n", name, e.getMessage());
        }
    }

}
