package SingleTest;//package <set your Memory package>;
import SingleTest.MyRunnable;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

public class PlayClass {

    static int deviceNum = 1;
    static int repetition =100;

    public static void main(String[] args) throws InterruptedException {

        for (int j = 0; j < repetition ; j++) {
            List<Thread> threads = new ArrayList<Thread>();

            for (int i = 0; i < deviceNum; i++) {
                Runnable task = new MyRunnable("localhost",8889,"contains(@os,'ios')");
                Thread worker = new Thread(task);
                worker.setName(String.valueOf(i));
                worker.start();
                threads.add(worker);
            }
            int running = 0;
            do {
                Thread.sleep(10000);
                running = 0;
                for (Thread thread : threads) {
                    if (thread.isAlive()) {
                        running++;
                    }
                }
                System.out.println("We have " + running + " running threads. ");
            } while (running > 0);
        }
    }
}

