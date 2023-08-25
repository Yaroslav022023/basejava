package com.basejava.webapp;

public class DeadLock {
    private static final Object LOCK_1 = new Object();
    private static final Object LOCK_2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (LOCK_1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                synchronized (LOCK_2) {
                    System.out.println("Succeeded to capture the monitor LOCK_2");
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (LOCK_2) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                synchronized (LOCK_1) {
                    System.out.println("Succeeded to capture the monitor LOCK_1");

                }
            }
        }).start();
    }
}