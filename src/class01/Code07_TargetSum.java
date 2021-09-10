package class01;

import java.util.HashMap;

/**
 * 给定一个数组arr，你可以在每个数字之前决定+或者-
 * 但是必须所有数字都参与
 * 再给定一个数target，请问最后算出target的方法数是多少？
 * leetcode 494
 */
public class Code07_TargetSum {
    public static int findWays1(int[] arr, int target) {
        return process1(arr, 0, target);
    }

    private static int process1(int[] arr, int index, int rest) {
        if (index == arr.length) {
            return rest == 0 ? 1 : 0;
        }
        int p1 = process1(arr, index + 1, rest - arr[index]);
        int p2 = process1(arr, index + 1, rest + arr[index]);
        return p1 + p2;
    }

    public static int findWays2(int[] arr, int target) {
        HashMap<Integer, HashMap<Integer,Integer>> dp = new HashMap<>();
        return process2(arr, 0, target, dp);
    }

    private static int process2(int[] arr, int index, int rest, HashMap<Integer,HashMap<Integer,Integer>> dp) {
        if (dp.containsKey(index) && dp.get(index).containsKey(rest)) {
            return dp.get(index).get(rest);
        }
        int ans;
        if (index == arr.length) {
            ans = rest == 0 ? 1 : 0;
        } else {
            int p1 = process2(arr, index + 1, rest - arr[index], dp);
            int p2 = process2(arr, index + 1, rest + arr[index], dp);
            ans = p1 + p2;
        }
        if (!dp.containsKey(index)) {
            dp.put(index,new HashMap<>());
        }
        dp.get(index).put(rest, ans);
        return ans;
    }
    // 根据业务优化
    // 1. 把arr转变为非负数
    // 2. 非负数累加和 < target 结果肯定不存在
    // 3. arr 内部不论如何加减，结果的奇偶性不变
    // 4.  比如说给定一个数组, arr = [1, 2, 3, 4, 5] 并且 target = 3
    // 其中一个方案是 : +1 -2 +3 -4 +5 = 3
    // 该方案中取了正的集合为P = {1，3，5}
    // 该方案中取了负的集合为N = {2，4}
    // 所以任何一种方案，都一定有 sum(P) - sum(N) = target
    // 现在我们来处理一下这个等式，把左右两边都加上sum(P) + sum(N)，那么就会变成如下：
    // sum(P) - sum(N) + sum(P) + sum(N) = target + sum(P) + sum(N)
    // 2 * sum(P) = target + 数组所有数的累加和
    // sum(P) = (target + 数组所有数的累加和) / 2
    // 也就是说，任何一个集合，只要累加和是(target + 数组所有数的累加和) / 2
    // 那么就一定对应一种target的方式
    // 也就是说，比如非负数组arr，target = 7, 而所有数累加和是11
    // 求有多少方法组成7，其实就是求有多少种达到累加和(7+11)/2=9的方法
    // 5. 二维动态规划的空间压缩技巧
    public static int findWays3(int[] arr, int target) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < 0) {
                arr[i] = 0-arr[i];
            }
            sum += arr[i];
        }

        return sum < Math.abs(target) || ((target & 1) ^ (sum & 1)) == 1 ? 0 : subset1(arr, (target + sum) >> 1);
    }

    // 不用空间压缩
    // 求nums有多少个子集累加和是s
    private static int subset1(int[] nums, int s) {
        int n =nums.length;
        // dp[i][j] nums前缀长度为 i 的所有子集，有多少累加和是 j
        int[][] dp = new int[n + 1][s + 1];
        dp[0][0] = 1; // nums前缀长度为0的所有子集，有多少累加和是0？一个：空集
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= s; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j - nums[i - 1] >= 0) {
                    dp[i][j] += dp[i - 1][j - nums[i - 1]];// nums[i - 1] i位置的数
                }
            }
        }
        return dp[n][s];
    }

    // 用空间压缩
    private static int subset2(int[] nums, int s) {
        int[] dp = new int[s + 1];
        dp[0] = 1;
        for (int n : nums) {
            for (int i = s; i >= n; i--) {
                dp[i] += dp[i - n];
            }
        }
        return dp[s];
    }
}
