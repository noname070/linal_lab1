package ru.ifmo.noname070.fait.linal.testers;

import java.util.function.BooleanSupplier;

public class SpeedTester implements ITester {

    public SpeedTester() {
    }

    @Override
    public void test(String name, BooleanSupplier testlink) {
        long startTime = System.nanoTime();
        try {
            System.out.printf("Тест: %-50s", name);
            boolean result = testlink.getAsBoolean();
            long endTime = System.nanoTime();

            System.out.print(
                    result ? "\u001B[32m" + "прошел успешно" + "\u001B[0m"
                            : "\u001B[31m" + "не прошел" + "\u001B[0m");

            long duration = endTime - startTime;
            System.out.printf("    Время выполнения: %6.5f сек\n", duration / 1_000_000_000.0);

        } catch (Exception e) {
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            System.out.printf("\u001B[33mвызвал исключение: %s\u001B[0m", e.getMessage());
            System.out.printf("    Время выполнения: %6.3f сек\n", duration / 1_000_000_000.0);
        }
    }
}
