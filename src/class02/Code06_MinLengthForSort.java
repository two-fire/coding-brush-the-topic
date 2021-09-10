package class02;

/**
 * 给定一个数组arr，只能对arr中的一个子数组排序，
 * 但是想让arr整体都有序
 * 返回满足这一设定的子数组中，最短的是多长
 * 思路：
 *      从左往右遍历，准备一个变量左边部分MAX，找到小于左MAX的最右边的值
 *      从右往左遍历，准备一个变量右边部分MIN，找到大于右MIN的最左边的值
 *      时间复杂度0(N) 空间复杂度O(1)
 */
public class Code06_MinLengthForSort {
    public static int minLength(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int leftMAX = arr[0];
        int index1 = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > leftMAX) {
                leftMAX = arr[i];
            } else if (arr[i] < leftMAX) {
                index1 = i;
            }
        }
        int rightMIN = arr[arr.length - 1];
        int index2 = arr.length - 1;
        for (int i = arr.length - 2; i >= 0; i--) {
            if (arr[i] < rightMIN) {
                rightMIN = arr[i];
            } else if (arr[i] > rightMIN) {
                index2 = i;
            }
        }
        return Math.max(0, index1 - index2 + 1);
    }
}
