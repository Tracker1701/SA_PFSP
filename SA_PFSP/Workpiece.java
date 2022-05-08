package com.SA_PFSP;

public class Workpiece {
    //自定义类：工件
    int being_processed;//正在被第几个机器加工,-1表示未被加工
    int left_time;//剩余加工时间

    Workpiece(){
        being_processed = -1;
        left_time = 0;
    }
    int beingPro(){
        return being_processed;
    } //返回正在被加工的机器号

    int leftTime(){
        return left_time;
    } //返回目前机器上的剩余加工时间

    void nextTime(){
        if (left_time > 0)
            left_time--;
    } //进入下一时间状态，可能处于空闲

    void nextMachine(int time){
        being_processed++;
        left_time = time;
    } //进入下一机器状态

    void reset(){
        being_processed = -1;
        left_time = 0;
    } //重置
}
