package class01;
import java.util.Arrays;
/**
 * 给定一个有序数组arr，代表依次坐落在x轴上
 * 有长度是L的绳子，能覆盖最多几个点
 * eg 绳子开头是1，L=5，绳子最右能推到5
 * 方法1：（二分）以每一个点x作为末尾，然后向左找多少点大于等于x-L，然后再加1 O(n*logn)
 * 方法2：（窗口）设置两个变量L,R，看绳子左边必须是L的情况下，R最多是几。然后L++，再看Ro(n)
 */
public class Code01_CordCoverMaxPoint {

        //    public static int maxPoint1(int[] arr, int L) {
//     int N = arr.length;
//     int max = Integer.MIN_VALUE;
//        for (int i = 0; i < N; i++) {
//            int j = i;
//            for (; j >= 0; j--) {
//                if (arr[i] - arr[j] > L) {
//                    break;
//                }
//            }
//            max = Math.max(max, i - j);
//        }
//        return max;
//    }
        public static int maxPoint1(int[] arr, int L) {
            int N = arr.length;
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < N; i++) {
                int nearest = nearestIndex(arr, i, arr[i] - L);
                max = Math.max(max, i - nearest + 1);
            }
            return max;
        }
        // arr顺序数组。二分找到R左边，不比value小的最左边的数的下标index
        private static int nearestIndex(int[] arr, int R, int value) {
            int L = 0;
            int index = R;
            while (L <= R) {
                int mid = L + ((R - L) >> 1);
                if (arr[mid] >= value) {
                    index = mid;
                    R = mid - 1;
                } else {
                    L = mid + 1;
                }
            }
            return index;
        }


        public static int maxPoint2(int[] arr, int len) {
            int R = 0;
            int ans = Integer.MIN_VALUE;
            for (int i = 0; i < arr.length; i++) {
                while (R < arr.length && arr[R] <= arr[i] + len) {
                    R++;
                }
                ans = Math.max(ans, R - i);
            }
            return ans;
        }
    // for test
    public static int test(int[] arr, int L) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            int pre = i - 1;
            while (pre >= 0 && arr[i] - arr[pre] <= L) {
                pre--;
            }
            max = Math.max(max, i - pre);
        }
        return max;
    }

    // for test
    public static int[] generateArray(int len, int max) {
        int[] ans = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * max);
        }
        Arrays.sort(ans);
        return ans;
    }

    public static void main(String[] args) {
        int len = 100;
        int max = 1000;
        int testTime = 100000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int L = (int) (Math.random() * max);
            int[] arr = generateArray(len, max);
            int ans1 = maxPoint1(arr, L);
            int ans2 = maxPoint2(arr, L);
            int ans3 = test(arr, L);
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.println("oops!");
                break;
            }
        }

    }

}
