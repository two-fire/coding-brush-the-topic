package class02;

import java.util.HashMap;

/**
 * 设计有setAll功能的哈希表 setALL 将所有的value全变为指定值
 * put、get、setAll方法，时间复杂度O(1)
 * 思路：利用一个时间戳变量,区分是setAll前的还是setAll后的
 */
public class Code05_SetAll {
    public static class MyValue<V> {
        private V value;
        private long time;
        public MyValue(V v,long t) {
            value = v;
            time = t;
        }
    }
    public static class MyHashMap<K, V> {
        private HashMap<K, MyValue<V>> map;
        private long time;
        private MyValue<V> setAll;
        public MyHashMap() {
            map = new HashMap<>();
            time = 0;
            setAll = new MyValue<>(null, -1);
        }

        public void put(K key, V value) {
            map.put(key, new MyValue<V>(value,time++));
        }
        public V get(K key) {
            if (!map.containsKey(key)) {
                return null;
            }
            if (map.get(key).time > setAll.time) {
                return map.get(key).value;
            } else {
                return setAll.value;
            }
        }
        public void setAll(V value) {
            if (!map.isEmpty()) {
                setAll=new MyValue<>(value, time++);
            }
        }
    }
}
