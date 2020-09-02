package com.bjh.annottion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class HttpsMethod {
    public static void main(String[] args) {
//        ArrayBlockingQueue<RobotState> arrayBlockingQueue = new ArrayBlockingQueue(3);
//
//        arrayBlockingQueue.add(new RobotState());
//        arrayBlockingQueue.add(new RobotState());
//        arrayBlockingQueue.add(new RobotState());
//
//        RobotState robot = arrayBlockingQueue.poll();
//        System.out.print(robot.isFree);


        List<String> list = new ArrayList<>();
        System.out.print(System.currentTimeMillis());
        for (int i=0;i<10000;i++){
            new RobotState();
        }
        System.out.print(System.currentTimeMillis());

    }
}
