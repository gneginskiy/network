package misc.runningsum;

import java.util.Arrays;

public class MyRunningSumFunctional implements IRunningSum {
    public int[] compute(int[] nums) {
        if (nums == null || nums.length == 0) return new int[0];
        int[] result = Arrays.copyOf(nums, nums.length);
        Arrays.parallelPrefix(result, Integer::sum);
        return result;
    }
}