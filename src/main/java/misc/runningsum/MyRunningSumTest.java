package misc.runningsum;

import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Collection;
import java.util.Set;

public class MyRunningSumTest {

    Collection<IRunningSum> runningSums = Set.of(
            new MyRunningSum(), new MyRunningSumFunctional());

    @Test(dataProvider = "casesProvider")
    public void test1(int[] inputArray, int[] expected) {
        for (IRunningSum runningSum : runningSums) {
            int[] actual = runningSum.compute(inputArray);
            Assert.assertEquals(actual, expected, runningSum.getClass() + "");
        }
    }

    @DataProvider
    public static Object[][] casesProvider() {
        return new Object[][]{
                {new int[]{}, new int[]{}},
                {null, new int[]{}},
                {new int[]{1, 2, 3}, new int[]{1, 3, 6}},
                {new int[]{0, 0, 0}, new int[]{0, 0, 0}},
                {new int[]{-1, -2, -3}, new int[]{-1, -3, -6}},
                {new int[]{-1, -2, 3}, new int[]{-1, -3, 0}},
        };
    }

}