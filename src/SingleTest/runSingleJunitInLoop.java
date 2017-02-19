
package SingleTest;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class runSingleJunitInLoop {

    public static void main(String[] args) {

        JUnitCore junit = new JUnitCore();
        Result result = null;

        for (int i = 0; i < 10; i++) {
            result = junit.run(SingleJunit.class);
        }

    }

}


