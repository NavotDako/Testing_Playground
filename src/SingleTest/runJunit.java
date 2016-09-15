package SingleTest;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class runJunit {

    public static void main(String[] args) throws IOException {
        int deviceNum = 2;
        String deviceQuery = "";

        File Report = new File("src/SingleTest/times.txt");
        Report.delete();

        for (int i = 0; i < deviceNum; ++i) {
            Runnable task = new run(deviceQuery);
            Thread worker = new Thread(task);
            worker.setName(String.valueOf(i));
            worker.start();
        }
    }
}

class run implements Runnable{
    int i;
    long[] times;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public run(String deviceQuery) {

    }

    @Override
    public void run() {
        long before = System.currentTimeMillis();
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(SingleJunit.class);
        times[i] =  System.currentTimeMillis() - before;
        try {
            if (result.getFailureCount()==0)
                Write(sdf.format(new Date(System.currentTimeMillis())) +" Iteration: "+i+" - "+times[i]);
            else Write(sdf.format(new Date(System.currentTimeMillis())) +" Iteration: "+i+" - FAILED @@@");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static void Write(String time) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("src/SingleTest/times.txt", true)));
        System.out.println(time);
        writer.append(time+"\n");
        writer.close();
    }
}

