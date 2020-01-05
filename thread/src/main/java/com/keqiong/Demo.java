package com.keqiong;

public class Demo {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        System.out.println("主线程逻辑结束了");

    }

}
class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0;i<10;i++){
            System.out.println("子逻辑中的i为" + i);
        }
    }
}
