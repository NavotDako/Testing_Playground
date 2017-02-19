package Play;

import com.experitest.client.Client;
import org.junit.*;


/**
 * Created by udi.valer on 9/27/2016.
 */
public class MultiTabTest {

    protected Client client = null;
    protected String iosVersion = "";
    protected String iosModel = "";
    protected boolean iphone = true; // Argument to understand if current test Runner oon deviceName or on iphone

    @Before
    public void setUp(){

        client = new Client("localhost", 8889, true);
        client.setProjectBaseDirectory(System.getProperty("user.dir"));
        client.setReporter("xml", "reports", "Untitled");
        //If you want to use set deviceName you are welcome


    }

    /** This test open the same web page 3 times, switch between them and in each page execute few different command to be sure the right dump is taken for each page.
     Passing between pages from switch application */
    @Test
    public void sameWindowMultipleTabs(){
        client.setDevice("ios_app:Dell iPhone");
        client.launch("com.apple.mobilesafari" , true , true);
        int numberOfPagesToOpen =10;
        int numberOfOpenPages = 0;

        /** Loop for opening 10 same web pages - www.google.com */
        do{
            client.sync(2000 , 80 , 10000);
            client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='URL' and (@class='UIAView' or @class='UIAElement')]", 0, "www.google.com");
            client.sendText("{ENTER}");

            client.sync(2000 , 80 , 10000);
            client.verifyElementFound("WEB", "xpath=//*[@id='hplogo']", 0);

            client.click("NATIVE", "xpath=//*[@text='Pages']", 0, 1);
            client.click("NATIVE", "xpath=//*[@text='New page']", 0, 1);
            numberOfOpenPages++;
        }while(numberOfOpenPages!=numberOfPagesToOpen);

    }

    @After
    public void tearDown(){

        client.generateReport(false);

        client.releaseClient();
    }


}



