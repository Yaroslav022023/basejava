package com.basejava.webapp;

import java.util.logging.Logger;

public class DeadLock {
    private static final Logger LOG = Logger.getLogger(DeadLock.class.getName());
    private static final Object LOCK_1 = new Object();
    private static final Object LOCK_2 = new Object();

    public static void main(String[] args) {
        actThread(LOCK_1, LOCK_2);
        actThread(LOCK_2, LOCK_1);
    }

    private static void actThread(Object lock1, Object lock2) {
        new Thread(() -> {
            LOG.info("Trying to lock Object 1 - " + Thread.currentThread().getName());
            synchronized (lock1) {
                LOG.info("Locked Object 1 - " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                LOG.info("Trying to lock Object 2 - " + Thread.currentThread().getName());
                synchronized (lock2) {
                    LOG.info("Locked Object 2 - " + Thread.currentThread().getName());
                }
            }
        }).start();
    }
}