package ru.ifmo.noname070.fait.linal.testers;

import java.util.function.BooleanSupplier;

public class SpeedTester extends Tester {

    public static void timeTest(String name, BooleanSupplier testlink) {

        long startTime = System.nanoTime();

        try {
            System.out.printf("Тест: %-50s", name);
            boolean result = testlink.getAsBoolean();
            long endTime = System.nanoTime();

            if (result) {
                System.out.print("\u001B[32m" + "прошел успешно" + "\u001B[0m");
            } else {
                System.out.print("\u001B[31m" + "не прошел" + "\u001B[0m");
            }

            long duration = endTime - startTime;
            System.out.printf("    Время выполнения: %6.3f сек\n", duration / 1_000_000_000.0);

        } catch (Exception e) {
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            System.out.printf("\u001B[33mвызвал исключение: %s\u001B[0m", e.getMessage());
            System.out.printf("    Время выполнения: %6.3f сек\n", duration / 1_000_000_000.0);
        }
    }

    public static void main(String[] args) {
        timeTest("testGetAndSetElement", () -> {
            return true;
        });

        timeTest("testAddition", () -> {
            return false;
        });

        timeTest("testMultiplication", () -> {
            throw new RuntimeException("Some exception!");
        });
    }
}