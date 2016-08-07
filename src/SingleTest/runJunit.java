package SingleTest;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class runJunit {
    public static void main(String[] args) throws IOException {
        int rep = 500;
        long[] times = new long[rep];
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        File Report = new File("src/SingleTest/times.txt");
        Report.delete();

        for (int i = 0; i < rep; i++) {
            long before = System.currentTimeMillis();
            JUnitCore junit = new JUnitCore();
            Result result = junit.run(Junit.class);
            times[i] =  System.currentTimeMillis() - before;
            if (result.getFailureCount()==0) Write(sdf.format(new Date(System.currentTimeMillis())) +" Iteration: "+i+" - "+times[i]);
            else Write(sdf.format(new Date(System.currentTimeMillis())) +" Iteration: "+i+" - FAILED @@@");
        }
    }

    private static void Write(String time) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("src/SingleTest/times.txt", true)));
        System.out.println(time);
        writer.append(time+"\n");
        writer.close();
    }
}
