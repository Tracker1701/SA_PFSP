package com.SA_PFSP;

public class Machine {//自定义类：机器
    int processing;//正在加工调度方案中的第几个零件,-1表示还未加工零件
    int left_time;//剩余加工时间
    Machine(){
        processing = -1;
        left_time = 0;
    }
    int leftTime(){
        return left_time;
    } //返回该机器当前加工零件的剩余加工时间

    int process(){
        return processing;
    } //返回正在加工调度方案中的第几个零件

    void nextTime(){
        if(left_time > 0)
            left_time--;
    } //进入下一时间状态，可能空闲

    void nextWP(int time){
        processing++;
        left_time = time;
    } //调度方案下一个工件就为

    void reset(){
        processing = -1;
        left_time = 0;
    } //重置状态
}

