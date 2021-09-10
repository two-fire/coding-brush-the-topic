package class01;

/**
 * 给定一个整数num，
 * 如何不用循环语句，
 * 返回>=num，并且离num最近的，2的某次方
 * n = num-1 把 n 是1的右边都填成1 再加1
 * 比如 n=5  0110  -> 0111
 * 如果num是负数，那最后n全是1 加了1就是0  最终返回2
 * >>> 不带符号右移
 * >>  带符号右移
 */
public class Code03_Near2Power {
    public static int tableSizeFor(int num) {
        if (num <= 0) {
            return 1;
        }
        int n = num - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n + 1;
    }

    public static void main(String[] args) {
        int n = 120;
        System.out.println(tableSizeFor(n));
    }
}
