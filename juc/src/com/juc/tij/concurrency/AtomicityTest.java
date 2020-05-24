package com.juc.tij.concurrency;

import java.util.concurrent.*;

public class AtomicityTest implements Runnable {

   private int i = 0;

    /*
    This operation is not guarded by synchronized, thus allows the value to be read
    while the object is in an unstable state
    Atomicity doesn't imply Synchronization
    On top of this, i is also not volatile, thus has visibility issue
     */

    public int getValue() { return i; }

    private synchronized void evenIncrement() { i++; i++; }
    public void run() {
        while(true)
        evenIncrement();
    }

    public static void main(final String[] args) {

        final ExecutorService exec = Executors.newCachedThreadPool();
        final AtomicityTest at = new AtomicityTest();
        exec.execute(at);

        while (true) {
            final int val = at.getValue();
                if(val % 2 != 0) {
                System.out.println(val);
                System.exit(0);
            }

        }
    }
}