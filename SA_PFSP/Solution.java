package com.SA_PFSP;


import java.io.*;
import java.util.Random;

import static com.SA_PFSP.simulate.Max_num_Machine;
import static com.SA_PFSP.simulate.Max_num_Workpiece;

public class Solution {
    public static final double T0 = 10000;
    public static final double theta = 0.99999;
    public static void main(String[] args) throws IOException {
        //导入数据
        int n = 0, m = 0;
        int[] p = new int[Max_num_Machine * Max_num_Workpiece];
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(new FileInputStream("input.txt"), "utf-8"));
            String hang = in.readLine();
            String[] mn = null;
            mn = hang.split("\\s+");
            n = Integer.parseInt(mn[0]);
            m = Integer.parseInt(mn[1]);    // 读入第一行的n和m
            for (int i = 0; i < n; i++){
                hang = in.readLine();
                String[] data_hang = null;
                data_hang = hang.split("\\s+");     //以多个空格,回车,换行等空白符来分割
                for (int j = 1; j <= m; j++)
                    p[i * m + j - 1] = Integer.parseInt(data_hang[2*j - 1]);
            }
            in.close();
//            调试时用
//            for (int i = 0; i < n; i++) {
//                for (int j = 0; j < m; j++)
//                    System.out.print(p[i * m + j] + " ");
//                System.out.println();
//            }
        }catch(FileNotFoundException e){System.out.println("File Not Found!");}

        /*初始化模拟模型*/
        simulate S = new simulate(n, m, p);
        S.randomReset();//随机初始化

        //模拟退火算法
        int t = 0x3f3f3f3f;
        int count = 0;  //用来记录连续未接受新解的次数
        int count1 = 0;

        double T = T0;
        for (int i = 0; ; i++) {
            System.out.println(i+"\t"+t);
            T = T * theta; //更新当前温度
            S.randomExchange(); //产生新解
            S.resetForNewRound();//重置工件机器状态

            int new_t = S.f();//计算新解的目标函数
            if (new_t < t) {//若新解的目标函数更小
                t = new_t;
                count = 0;
                count1 = 0;
            }//接受新解
            else {
                Random r = new Random();
                double pp = Math.exp((t - new_t) / T);
                if (r.nextDouble()  <= pp) {
                    if(t==new_t)
                        count1++;
                    t = new_t;
                    count = 0;
                }//以概率接受新解
                else {
                    S.rollBack();//回退至上一状态
                    count++;
                    if(t==new_t)
                        count1++;
                    if (count > 50 || count1>50) //如果长时间未接受新解，结束搜索
                        break;
                }//拒绝新解
            }
        }
        System.out.println(t);
        S.resetForNewRound();
        S.f();
        S.outputdata();
    }
}
