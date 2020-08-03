package misc.runningsum;


public class MyRunningSum implements IRunningSum {
    public int[] compute(int[] nums) {
        if (nums == null || nums.length == 0) return new int[0];
        int[] result = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            result[i] = i == 0 ? nums[i] : nums[i] + result[i - 1];
        }
        return result;
    }

}