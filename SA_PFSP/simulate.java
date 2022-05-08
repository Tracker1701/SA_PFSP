package com.SA_PFSP;

import java.io.*;
import java.util.Random;

public class simulate {
    public static final int Max_num_Machine = 30;
    public static final int Max_num_Workpiece = 60;
    int[][] cost = new int[Max_num_Workpiece][Max_num_Machine];  //时间花费
    int[] management = new int[Max_num_Workpiece]; //调度方案
    int[][] Time = new int[Max_num_Machine][Max_num_Workpiece]; //记录每个机器的调度方案中的零件开始加工时间
    int Wnum, Mnum; //工件，机器数量
    int switchx, switchy; //记录交换信息，便于回退至上一状态
    Machine[] M = new Machine[Max_num_Machine]; //机器集合
    Workpiece[] W = new Workpiece[Max_num_Workpiece]; //工件集合

    simulate(int n, int m, int[] p){
        Wnum = n;     Mnum = m;
        switchx = 0;    switchy = 0;
        for (int i = 0; i < Wnum; i++)
            if (Mnum >= 0) System.arraycopy(p, i * Mnum, cost[i], 0, Mnum);
        for (int j = 0; j < Wnum; j++)
            management[j] = j;       //初始化调度方案
        for (int i = 0; i < Mnum; i++)
            for (int j = 0; j < Wnum; j++)
                Time[i][j] = 0;     //将每个机器的调度方案中的零件开始加工时间初始化为0
    }

    void randomReset(){
        Random r = new Random();
        for (int j = 0; j < Wnum; j++) {
            int x = r.nextInt(Wnum - j) + j;
            int temp = management[j];
            management[j] = management[x];
            management[x] = temp;
        }
    } //随机初始化整个调度方案

    void rollBack(){
        int temp = management[switchx];
        management[switchx] = management[switchy];
        management[switchy] = temp;
    } //回退至上一状态

    void randomExchange(){
        Random r = new Random();
        switchx = r.nextInt(Wnum);
        switchy = r.nextInt(Wnum);
        int temp = management[switchx];
        management[switchx] = management[switchy];
        management[switchy] = temp;
    } //随机交换某两个零件的加工顺序

    int f(){
        int rtime; //模拟时间进行
        for (rtime = 0; ; rtime++) {
            for (int i = 0; i < Mnum; i++) {
                int processing = M[i].process();
                if (processing != Wnum - 1) { //不是最后一个工件
                    int x = management[processing + 1];
                    if (M[i].leftTime() == 0 && W[x].leftTime() == 0 && W[x].beingPro() == i - 1) {
                        //下一个工件空闲，并且已加工至当前机器
                        M[i].nextWP(cost[x][i]);//下一个工件就位
                        W[x].nextMachine(cost[x][i]);//工件进入下一机器
                        Time[i][processing + 1] = rtime ; // 记录开始时间
                    }
                }
            }
            //工件以及机器都进入下一时间状态
            for (int i = 0; i < Mnum; i++)
                M[i].nextTime();
            for (int i = 0; i < Wnum; i++)
                W[i].nextTime();
            //检验是否加结束加工
            int flag = 0;
            for (int i = 0; i < Wnum; i++) {
                if (W[i].leftTime() == 0 && W[i].beingPro() == Mnum - 1)
                    continue;
                else {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                rtime++;
                break;//结束
            }
        }
        return rtime;
    }//模拟一次加工，返回加工时间，并且维护Time数组

    void outputdata(){//输出调度方案，时间方案
        StringBuilder management_str = new StringBuilder();
        for (int i = 0; i < Mnum; i++) {
            for (int j = 0; j < Wnum; j++) {
                management_str.append(management[j]).append("\t");
            }
            management_str.append("\n");
        }
        String fileName = "management.txt";
        //将调度方案写入文件
        File file = new File(fileName);
        if(file.exists()) {
            System.out.println("文件已存在，将覆盖已有文件！");
        }else {
            try {
                file.createNewFile();
                System.out.println("创建"+fileName);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fw =new FileWriter(file);
            fw.write(String.valueOf(management_str));
            fw.flush();
            fw.close();
            System.out.println("写入文件成功");
        }catch(IOException e) {
            e.printStackTrace();
        }

        StringBuilder time_str = new StringBuilder();
        for (int i = 0; i < Mnum; i++) {
            for (int j = 0; j < Wnum; j++) {
                time_str.append(Time[i][j]).append("\t").append(Time[i][j] + cost[management[j]][i]).append("\t");
            }
            time_str.append("\n");
        }

        fileName = "time.txt";
        //将每个机器的调度方案中的零件开始加工时间写入文件
        file = new File(fileName);
        if(file.exists()) {
            System.out.println("文件已存在，将覆盖已有文件！");
        }else {
            try {
                file.createNewFile();
                System.out.println("创建"+fileName);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fw =new FileWriter(file);
            fw.write(String.valueOf(time_str));
            fw.flush();
            fw.close();
            System.out.println("写入文件成功");
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    void resetForNewRound(){
        for (int i = 0; i < Wnum; i++)
            if(W[i] != null)
                W[i].reset();
            else
                W[i] = new Workpiece();
        for (int i = 0; i < Mnum; i++)
            if(M[i] != null)
                M[i].reset();
            else
                M[i] = new Machine();

    }//重置所有状态，准备下一次模拟

}

