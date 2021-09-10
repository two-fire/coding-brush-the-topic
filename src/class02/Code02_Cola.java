package class02;

/**
 * 贩卖机只支持硬币支付，且收退都只支持10 ，50，100三种面额
 * 一次购买只能出一瓶可乐，且投钱和找零都遵循优先使用大钱的原则
 * 需要购买的可乐数量是m，
 * 其中手头拥有的10、50、100的数量分别为a、b、c
 * 可乐的价格是x(x是10的倍数)
 * 请计算出需要投入硬币次数？
 * 思路：用10面额全买可乐，用50全买可乐... 看看m在哪个区间
 * 注：
 *      a/x 向上取整  =>  (a + x - 1) / x
 */
public class Code02_Cola {
    // 暴力尝试
    public static int right(int m, int a, int b, int c, int x) {
        int[] money = {100,50,10};
        int[] count = {c,b,a};
        int puts = 0;
        while (m != 0) {
            int cur = buy(money,count, x);
            if (cur == -1) {
                return -1;
            }
            puts += cur;
            m--;
        }
        return puts;
    }

    private static int buy(int[] money, int[] count, int rest) {
        int first = -1;
        for (int i = 0; i < 3; i++) {
            if (count[i] != 0) {
                first = i;
                break;
            }
        }
        if (first == -1) { // 没有钱了
            return -1;
        }
        if (money[first] >= rest) { // 一张就能买到
            count[first]--;
            // 从first+1开始找零
            giveRest(money, count, first+1,money[first]-rest,1);
            return 1;
        } else {
            count[first]--;
            int next = buy(money,count,rest-money[first]);
            if (next == -1) {
                return -1;
            }
            return 1 + next;
        }
    }

    private static void giveRest(int[] money, int[] count, int i, int oneTimeRest, int times) {
        for (;i < 3; i++) {
            count[i] += (oneTimeRest / money[i]) * times;
            oneTimeRest %= money[i];
        }
    }

    // 正式方法
    public static int putTimes(int m, int a, int b, int c, int x) {
        int[] money = {100,50,10};
        int[] count = {c,b,a};
        int puts = 0;
        int preMoneyRest = 0; // 之前面值的钱还剩多少总钱数
        int preCountRest = 0; // 之前面值的钱还剩多少总张数
        for (int i = 0; i < 3 && m != 0; i++) {
            // 当前面值参与支付第一瓶可乐，需要多少张
            int curMoneyFirstBuyCount = (x - preMoneyRest + money[i] - 1) / money[i];
            if (count[i] >= curMoneyFirstBuyCount) { // 支付得起
                // 找零
//                giveRest(money,count,i+1,(curMoneyFirstBuyCount * money[i] - (x - preCountRest)),1);
                giveRest(money,count,i+1,(preMoneyRest + money[i] * curMoneyFirstBuyCount) - x,1);
                puts += curMoneyFirstBuyCount + preCountRest;
                count[i] -= curMoneyFirstBuyCount;
                m--;
            } else { // 张数不够完全支付
                preCountRest += count[i];
                preMoneyRest += money[i] * count[i];
                continue;
            }
            // 凑出第一瓶可乐，可能还能继续买
            // 用当前面值的钱，买一瓶可乐需要几张
            int curMoneyBuyOneColaCount = (x + money[i] - 1) / money[i];
            // 用当前面值的钱能买几瓶可乐
            int curMoneyBuyColas = Math.min(m, count[i] /curMoneyBuyOneColaCount);
            // 用当前面值的钱，买一瓶可乐剩余多少钱
            int oneTimeRest = money[i] * curMoneyBuyOneColaCount - x;
            // 一起找零
            giveRest(money,count,i+1,oneTimeRest,curMoneyBuyColas);
            puts += curMoneyBuyColas * curMoneyBuyOneColaCount;
            m -= curMoneyBuyColas;
            count[i] -= curMoneyBuyOneColaCount * curMoneyBuyColas;
            preCountRest = count[i];
            preMoneyRest = count[i] * money[i];
        }
        return m == 0 ? puts : -1;
    }
    public static void main(String[] args) {
        int testTime = 1000;
        int zhangMax = 10;
        int colaMax = 10;
        int priceMax = 20;
        System.out.println("如果错误会打印错误数据，否则就是正确");
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int m = (int) (Math.random() * colaMax);
            int a = (int) (Math.random() * zhangMax);
            int b = (int) (Math.random() * zhangMax);
            int c = (int) (Math.random() * zhangMax);
            int x = ((int) (Math.random() * priceMax) + 1) * 10;
            int ans1 = putTimes(m, a, b, c, x);
            int ans2 = right(m, a, b, c, x);
            if (ans1 != ans2) {
                System.out.println("int m = " + m + ";");
                System.out.println("int a = " + a + ";");
                System.out.println("int b = " + b + ";");
                System.out.println("int c = " + c + ";");
                System.out.println("int x = " + x + ";");
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
