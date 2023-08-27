package com.basejava.webapp;

public class DeadLock {
    private static final Object LOCK_1 = new Object();
    private static final Object LOCK_2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> actThread(LOCK_1, LOCK_2)).start();
        new Thread(() -> actThread(LOCK_2, LOCK_1)).start();
    }

    private static void actThread(Object lock1, Object lock2) {
        synchronized (lock1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (lock2) {
                System.out.println("Succeeded to capture the monitor LOCK_2");
            }
        }
    }
}