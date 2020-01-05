package com.keqiong;

public class SourceConflict {
    public static void main(String[] args) {
        Runnable r = () -> {
            while (TicketCenter.restCount > 0) {
                System.out.println(Thread.currentThread().getName()+"余票为"+ --TicketCenter.restCount + "张");
            }
        };
        Thread r1 = new Thread(r,"threda1");
        Thread r2 = new Thread(r,"threda2");
        Thread r3 = new Thread(r,"threda3");
        Thread r4 = new Thread(r,"threda4");
        r1.start();
        r2.start();
        r3.start();
        r4.start();


    }
}
class TicketCenter {
    public static int restCount = 100;
}
