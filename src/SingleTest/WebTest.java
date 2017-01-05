package SingleTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.experitest.client.Client;

public class WebTest {

    private String host = "localhost";
    private int port = 8889;
    private String projectBaseDirectory =System.getProperty("user.dir");
    protected Client client = null;
    private String deviceOs="ios";


    @Before
    public void setUp(){
        client = new Client(host, port, true);
        client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "reports", "Untitled");
    }

    @Test
    public void testWebTabs()
    {

        client.waitForDevice("@os='"+ deviceOs+"'", 30000);

        // launch ebay
        if(deviceOs.contains("ios"))
            client.launch("safari:http://www.ebay.com", true, false);
        // ebay test
        ebayTest("back bag");

        // add new tab and open espn
        if(deviceOs.equals("ios"))
        {
            client.click("NATIVE", "xpath=//*[@text='Pages']", 0, 1);
            if(client.getProperty("device.model").contains("Pad"))
                client.click("NATIVE", "xpath=//*[@text='New tab']", 0, 1);
            else
                client.click("NATIVE", "xpath=//*[@text='New page']", 0, 1);
            client.elementSendText("NATIVE", "xpath=//*[@text='Address']", 0, "http://www.google.co.il");

        }

        client.sendText("{Enter}");


        // espn site test
        GoogleTest("Manchester United");

        // switch tab
        switchTabIos(1);
        ebayTest("manchester united");
        switchTabIos(2);
        GoogleTest("Chelsea");

    }



    private void switchTabIos(int i) {
        if(deviceOs.equals("ios"))
            client.click("NATIVE", "xpath=//*[@text='Pages']", 0, 1);
        // switch to tab 1
        if(i==1)
        {
            client.click("NATIVE", "xpath=//*[@text='Electronics, Cars, Fashion, Collectibles, Coupons and More | eBay']", 0, 1);

        }
        // switch to tab 2
        else
            client.click("NATIVE", "xpath=//*[@text='Google']", 0, 1);
    }


    void ebayTest(String stuffTosearch)
    {


        if(client.getProperty("device.model").contains("Pad")){
            client.elementSendText("WEB", "xpath=//*[@id='gh-ac']", 0, stuffTosearch);
            client.click("WEB", "xpath=//*[@id='gh-btn']", 0, 1);
            client.waitForElement("WEB", "xpath=//*[@text='Auction']", 0, 30000);
            client.click("WEB", "xpath=//*[@id='gh-la']", 0, 1);

        }
        else{
            client.elementSendText("WEB", "xpath=//*[@id='kw']", 0, stuffTosearch);
            client.click("WEB", "xpath=//*[@id='searchTxtBtn']", 0, 1);
            client.waitForElement("WEB", "xpath=//*[@text='Auction']", 0, 30000);
            client.click("WEB", "xpath=//*[@id='gh-mlogo']", 0, 1);
        }


    }

    void GoogleTest(String searchTeam)
    {
        client.elementSendText("WEB", "xpath=//*[@id='lst-ib']", 0,searchTeam );
        client.click("WEB", "xpath=//*[@name='btnG']", 0, 1);
        client.click("WEB", "xpath=//*[@class='_Ejh']", 0, 1);

    }


    @After
    public void tearDown(){
        // Generates a report of the test case.
        // For more information - https://docs.experitest.com/display/public/SA/Report+Of+Executed+Test
        client.generateReport(false);
        // Releases the client so that other clients can approach the agent in the near future. 
        client.releaseClient();
    }

}