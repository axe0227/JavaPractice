package com.juc.tij.concurrency;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class SleepBlocked implements Runnable {
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch(InterruptedException e) {
            print("InterruptedException");
        }
        print("Exiting SleepBlocked.run()");
    }

    private void print(String s) {
        System.out.println(s);
    }
}

class IOBlocked implements Runnable {
    private InputStream in;
    public IOBlocked(InputStream is) { in = is; }
    public void run() {
        try {
            print("Waiting for read():");
            in.read();
        } catch(IOException e) {
            if(Thread.currentThread().isInterrupted()) {
                print("Interrupted from blocked I/O");
            } else {
                throw new RuntimeException(e);
            }
        }
        print("Exiting IOBlocked.run()");
    }

    private void print(String s) {
        System.out.println(s);
    }
}
class SynchronizedBlocked implements Runnable {
    public synchronized void f() {
        while(true) // Never releases lock
            Thread.yield();
    }
    public SynchronizedBlocked() {
        new Thread() {
            public void run() {
                f(); // Lock acquired by this thread
            }
        }.start();
    }
    public void run() {
        print("Trying to call f()");
        f();
        print("Exiting SynchronizedBlocked.run()");
    }

    private void print(String s) {
        System.out.println(s);
    }
}
public class Interruption {

    private static ExecutorService exec =
            Executors.newCachedThreadPool();
    static void test(Runnable r) throws InterruptedException{
        Future<?> f = exec.submit(r);
        TimeUnit.MILLISECONDS.sleep(100);
        print("Interrupting " + r.getClass().getName());
        f.cancel(true); // Interrupts if running
        print("Interrupt sent to " + r.getClass().getName());
    }

    private static void print(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) throws Exception {


        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SynchronizedBlocked());

        TimeUnit.SECONDS.sleep(3);
        print("Aborting with System.exit(0)");
        System.exit(0); // ... since last 2 interrupts failed
    }


    static class NIOBlocked implements Runnable {
        private final SocketChannel sc;

        public NIOBlocked(SocketChannel sc) {
            this.sc = sc;
        }

        public void run() {
            try {
                print("Waiting for read() in " + this);
                sc.read(ByteBuffer.allocate(1));
            } catch (ClosedByInterruptException e) {
                print("ClosedByInterruptException");
            } catch (AsynchronousCloseException e) {
                print("AsynchronousCloseException");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            print("Exiting NIOBlocked.run() " + this);
        }
    }

    static class NIOInterruption {
        public static void main(String[] args) throws Exception {
            ExecutorService exec = Executors.newCachedThreadPool();
            ServerSocket server = new ServerSocket(8080);
            InetSocketAddress isa =
                    new InetSocketAddress("localhost", 8080);
            SocketChannel sc1 = SocketChannel.open(isa);
            SocketChannel sc2 = SocketChannel.open(isa);
            Future<?> f = exec.submit(new NIOBlocked(sc1));
            exec.execute(new NIOBlocked(sc2));
            exec.shutdown();
            TimeUnit.SECONDS.sleep(1);
// Produce an interrupt via cancel:
            f.cancel(true);
            TimeUnit.SECONDS.sleep(1);
// Release the block by closing the channel:
            sc2.close();
        }
    }

}
