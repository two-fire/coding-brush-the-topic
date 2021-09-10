package class01;

import java.util.Arrays;

/**
 * 给定两个非负数组x和hp，长度都是N，再给定一个正数range
 * x有序，x[i]表示i号怪兽在x轴上的位置；hp[i]表示i号怪兽的血量
 * 再给定一个正数range，表示如果法师释放技能的范围长度
 * 被打到的每只怪兽损失1点血量。
 * 返回要把所有怪兽血量清空，至少需要释放多少次AOE技能？
 */
public class Code06_AOE {
    // 贪心：永远让最左边缘以最优的方式(AOE尽可能往右扩，让最左边缘盖住目前怪的最左)变成0
    // 也就是选择：一定能覆盖到最左边缘, 但是尽量靠右的中心点
    // 不用线段树
    public static int minAoe2(int[] x, int[] hp, int range) {
        int N = x.length;
        int ans = 0;
        for (int i = 0; i < N; i++) {
            if (hp[i] > 0) {
                int trigger = i;
                while (trigger < N && x[trigger] - x[i] <= range) {
                    trigger++;
                }
                ans += hp[i];
                aoe(x, hp, i, trigger - 1, range); // 把trigger为中心点的这一段减到 hp[i]=0
            }
        }
        return ans;
    }
    private static void aoe(int[] x, int[] hp, int L, int trigger, int range) {
        int N = x.length;
        int RPost = trigger;
        while (RPost < N && x[RPost] -x[trigger] <= range) {
            RPost++;
        }
        int minus = hp[L];
        for (int i = L; i < RPost; i++) {
            hp[i] = Math.max(0, hp[i] - minus);
        }
    }

    // 用线段树 时间复杂度O(N * logN)
    public static int minAoe3(int[] x, int[] hp, int range) {
        int N = x.length;
        // coverLeft[i]：如果以i为中心点放技能，左侧能影响到哪，下标从1开始，不从0开始
        // coverRight[i]：如果以i为中心点放技能，右侧能影响到哪，下标从1开始，不从0开始
        int[] coverLeft = new int[N + 1];
        int[] coverRight = new int[N + 1];
        int left = 0;
        int right = 0;
        for (int i = 0; i < N; i++) {
            while (x[i] - x[left] > range) {
                left++;
            }
            while (right < N && x[right] - x[i] <= range) {
                right++;
            }
            coverLeft[i + 1] = left + 1;
            coverRight[i + 1] = right;
        }
        // best[i]: 如果i是最左边缘点，选哪个点做技能中心点最好，下标从1开始，不从0开始
        int[] best = new int[N + 1];
        int trigger = 0;
        for (int i = 0; i < N; i++) {
            while (trigger < N && x[trigger] - x[i] <= range) {
                trigger++;
            }
            best[i + 1] = trigger;
        }
        SegmentTree st = new SegmentTree(hp);
        st.build(1, N, 1);
        int ans = 0;
        for (int i = 1; i <= N; i++) {
            // 查询当前位置怪物是否存活
            long leftEdge = st.query(i, i, 1, N, 1);
            if (leftEdge > 0 ) {
                // t = best[i]: 在哪放技能最值
                ans += leftEdge;
                int t = best[i];
                int l = coverLeft[t];
                int r = coverRight[t];
                // 同时[l...r]整个范围，所有的怪物都会扣除掉leftEdge的血量
                st.add(l,r,(int)(-leftEdge),1,N,1);
            }
        }
        return ans;
    }


    public static class SegmentTree {
        private int MAXN;
        private int[] arr;
        private int[] sum;
        private int[] lazy;

        public SegmentTree(int[] x) {
            MAXN = x.length + 1;
            arr = new int[MAXN];
            for (int i = 1; i < MAXN; i++) {
                arr[i] = x[i - 1];
            }
            sum = new int[MAXN << 2];
            lazy = new int[MAXN << 2];
        }

        public void build(int l, int r, int rt) {
            if (l == r) {
                sum[rt] = arr[l];
                return;
            }
            int mid = (r + l) >> 1;
            build(l, mid, rt << 1);
            build(mid + 1, r, rt << 1 | 1);
            pushUp(rt);
        }

        private void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }

        private void pushDown(int rt, int ln, int rn) {
            if (lazy[rt] != 0) {
                lazy[rt << 1] += lazy[rt];
                lazy[rt << 1 | 1] += lazy[rt];
                sum[rt << 1] += lazy[rt] * ln;
                sum[rt << 1 | 1] += lazy[rt] * rn;
                lazy[rt] = 0;
            }
        }

        public void add(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && R >= r) {
                sum[rt] += C * (r - l + 1);
                lazy[rt] += C;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            if (L <= mid) {
                add(L, R, C, l, mid, rt << 1);
            }
            if (R > mid) {
                add(L, R, C, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);
        }

        public long query(int L, int R, int l, int r, int rt) {
            if (L <= l && R >= r) {
                return sum[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid -l+ 1, r-mid);
            long ans = 0;
            if (L <= mid) {
                ans += query(L,R,l,mid,rt<<1);
            }
            if (R > mid) {
                ans += query(L,R,mid + 1,r,rt<<1 | 1);
            }
            return ans;
        }
    }
    // for test
    public static int[] randomArray(int n, int valueMax) {
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = (int) (Math.random() * valueMax) + 1;
        }
        return ans;
    }

    // for test
    public static int[] copyArray(int[] arr) {
        int N = arr.length;
        int[] ans = new int[N];
        for (int i = 0; i < N; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    public static void main(String[] args) {
        int N = 500;
        int X = 10000;
        int H = 50;
        int R = 10;
        int time = 5000;
        System.out.println("test begin");
        for (int i = 0; i < time; i++) {
            int len = (int) (Math.random() * N) + 1;
            int[] x = randomArray(len, X);
            Arrays.sort(x);
            int[] hp = randomArray(len, H);
            int range = (int) (Math.random() * R) + 1;
            int[] x2 = copyArray(x);
            int[] hp2 = copyArray(hp);
            int ans2 = minAoe2(x2, hp2, range);
            // 已经测过下面注释掉的内容，注意minAoe1非常慢，
            // 所以想加入对比需要把数据量(N, X, H, R, time)改小
//			int[] x1 = copyArray(x);
//			int[] hp1 = copyArray(hp);
//			int ans1 = minAoe1(x1, hp1, range);
//			if (ans1 != ans2) {
//				System.out.println("Oops!");
//				System.out.println(ans1 + "," + ans2);
//			}
            int[] x3 = copyArray(x);
            int[] hp3 = copyArray(hp);
            int ans3 = minAoe3(x3, hp3, range);
            if (ans2 != ans3) {
                System.out.println("Oops!");
                System.out.println(ans2 + "," + ans3);
            }
        }
        System.out.println("test end");
    }
}
