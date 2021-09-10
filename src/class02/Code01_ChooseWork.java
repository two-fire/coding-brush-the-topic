package class02;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * 给定数组hard和money，长度都为N
 * hard[i]表示i号的难度， money[i]表示i号工作的收入
 * 给定数组ability，长度都为M，ability[j]表示j号人的能力
 * 每一号工作，都可以提供无数的岗位，难度和收入都一样
 * 但是人的能力必须>=这份工作的难度，才能上班
 * 返回一个长度为M的数组ans，ans[j]表示j号人能获得的最好收入
 *
 * 思路：把工作封装，难度小的排前面，难度一样的只保留收入最大的
 * 难度大收入小的也删掉
 * 有序表来进行查询（treeMap 根据key难度从小到大排序 只保留没删的工作）
 *   可以快速查到<= x离x最近的，也可以查询>=x 离x最近的。O(logN)
 */

public class Code01_ChooseWork {
    public static class Job {
        private int hard;
        private int money;
        public Job(int h, int m) {
            hard = h;
            money = m;
        }
    }

    public static class JobComparotor implements Comparator<Job> {
        @Override
        public int compare(Job o1, Job o2) {
            return  o1.hard != o2.hard ? o1.hard - o2.hard : (o2.money - o1.money);
        }
    }

    public static int[] bestIncome(int N, int[] hard, int[] money, int[] ability) {
        Job[] jobs = new Job[N];
        for (int i = 0; i < N; i++) {
            jobs[i] = new Job(hard[i], money[i]);
        }
        return getMoney(jobs, ability);
    }

    private static int[] getMoney(Job[] jobs, int[] ability) {
        Arrays.sort(jobs, new JobComparotor());
        TreeMap<Integer, Integer> map = new TreeMap();
        map.put(jobs[0].hard, jobs[0].money);
        Job pre = jobs[0];
        for (int i = 1; i < jobs.length; i++) {
            if (jobs[i].hard != pre.hard && jobs[i].money > pre.money) {
                map.put(jobs[i].hard,jobs[i].money);
                pre = jobs[i];
            }
        }
        int[] ans = new int[jobs.length];
        for (int i = 0; i < ability.length; i++) {
            Integer key = map.floorKey(ability[i]); //当前人的能力 <= ability[i]  且离它最近的
            ans[i] = key != null ? key : 0;
        }
        return ans;
    }
}
