package misc.armstrong;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ArmstrongNumbers {
    private static final int N_CORES = 4;

    public static void main(String[] args) {
        System.out.println(getArmstrongNumbers(1_000_000_000));
        //testTime(() -> getArmstrongNumbers(1_000_000_000));
    }

    private static void testTime(Runnable methodToTest) {
        long start = System.currentTimeMillis();
        methodToTest.run();
        long end = System.currentTimeMillis();
        System.out.println("execution time " + (end - start));
    }

    private static List<Integer> getArmstrongNumbers(int lastNumParam) {
        long start = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(N_CORES);

        final int[][] cachedPowers = getCachedPowers();
        List<Integer> result = new CopyOnWriteArrayList<>();

        int delta = lastNumParam / N_CORES;

        int from = 0;
        int to = delta;
        while (to <= lastNumParam) {
            runTask(executorService, cachedPowers, result, from, to);
            from = to;
            to += delta;
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            //do nothing
        }
        long end = System.currentTimeMillis();
        System.out.println("execution time " + (end - start));

        return result;
    }

    private static void runTask(ExecutorService executorService,
                                int[][] cachedPowers,
                                List<Integer> result,
                                int fromm, int too) {
        executorService.submit(
                () -> getIntegers(
                        fromm, too, cachedPowers, result));
    }

    private static List<Integer> getIntegers(int firstNum, int lastNum, int[][] cachedPowers, List<Integer> result) {
        for (int num = firstNum; num < lastNum; num++) {
            if (isArmstrongNumber(num, getDigits(num), cachedPowers)) {
                result.add(num);
            }
        }
        return result;
    }

    private static int[] getDigits(int num) {
        int[] digits = new int[getDigitsCount(num)];
        int index = 0;
        while (num != 0) {
            digits[index++] = num % 10;
            num /= 10;
        }
        return digits;
    }

    private static boolean isArmstrongNumber(int num, int[] digits, int[][] cachedPowers) {
        final int digitsCount = digits.length;
        int sumOfPowers = 0;
        for (int i = digitsCount - 1; i >= 0; i--) {
            int digit = digits[i];
            sumOfPowers += cachedPowers[digit][digitsCount];
        }
        return sumOfPowers == num;
    }

    private static int getDigitsCount(int num) {
        if (num < 10) return 1;
        if (num < 100) return 2;
        if (num < 1000) return 3;
        if (num < 10000) return 4;
        if (num < 100000) return 5;
        if (num < 1000000) return 6;
        if (num < 10000000) return 7;
        if (num < 100000000) return 8;
        if (num < 1000000000) return 9;
        return 10;
    }

    private static int[][] getCachedPowers() {
        int[][] ints = new int[10][11];
        for (int digit = 0; digit < 10; digit++) {
            int result = digit;
            for (int power = 1; power <= 10; power++) {
                ints[digit][power] = result*=digit;
            }
        }
        return ints;
    }
}

