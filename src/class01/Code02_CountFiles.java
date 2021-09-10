package class01;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 给一个文件目录的路径，写一个函数统计这个目录下所有的文件数量并返回
 * 隐藏文件也算，文件夹不算
 * 宽度优先：队列
 *      1）从队列中弹出文件夹
 *      2）如果是文件，count++，如果是文件夹，放到队列中
 * 这道题也可以用栈
 */
public class Code02_CountFiles {
    public static int getFileNums(String path) {
        File root = new File(path);
        if (!root.isDirectory() && !root.isFile()) {
            return 0;
        }
        if (root.isFile()) {
            return 1;
        }
        Queue<File> queue = new LinkedList<>();
        queue.add(root);
        int count = 0;
        while (!queue.isEmpty()) {
            File cur = queue.poll();
            for (File next : cur.listFiles()) {
                if (next.isDirectory()) {
                    queue.add(next);
                }
                if (next.isFile()) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\Lenovo\\Pictures\\活动";
        System.out.println(getFileNums(path));
    }


}
