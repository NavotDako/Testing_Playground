package Tests;
import FrameWork.AbsTest;
import FrameWork.MyClient;

/**
 * Created by richi.lebovich on 9/4/2016.
 */
public class OfficeDepot extends AbsTest{

    public OfficeDepot(MyClient client, String deviceQurey, int repNum, String reportFolder, String deviceOS, String testName) {
        super(client, deviceQurey, repNum, reportFolder, deviceOS, testName);
    }

    @Override
    protected void AndroidRunTest() {
        if(client.uninstall("com.officedepot.mobile.ui")){
            // If statement
        }
//        String str1 = client.getCurrentApplicationName();
        if(client.install("http://192.168.2.72:8181/AndroidApps/Office%20Depot.apk", true, false)){
            // If statement
        }
        client.launch("com.officedepot.mobile.ui/.home.SplashScreenActivity", false, true);
        if(client.applicationClose("com.officedepot.mobile.ui")){
            // If statement
        }
        client.launch("com.officedepot.mobile.ui/.home.SplashScreenActivity", false, true);
        client.click("NATIVE", "xpath=//*[@text='BROWSE PRODUCT']", 0, 1);
        client.click("NATIVE", "xpath=//*[@text='Furniture']", 0, 1);
        if(client.swipeWhileNotFound("Down", 0, 2000, "NATIVE", "xpath=//*[@text='Tables']", 0, 1000, 5, true)){
            // If statement
        }
        client.click("NATIVE", "xpath=//*[@id='browsehierarchy_list']/*[2]/*[2]", 0, 1);
        if(client.swipeWhileNotFound("Down", 0, 2000, "NATIVE", "xpath=//*[@text=concat('Lumisource Spyra Color-Changing Bar Table, 42', '\"', 'H x 1', '\"', 'W x 23', '\"', 'D, Clear/Chrome')]", 0, 1000, 10, true)){
            // If statement
        }

        if(client.isFoundIn("NATIVE", "xpath=//*[@id='action_bar_container']", 0, "Inside", "NATIVE", "xpath=//*[@contentDescription='More options']", 0, 0)){
            client.click("NATIVE", "xpath=//*[@contentDescription='More options']", 0, 1);
            client.click("NATIVE", "xpath=//*[@text='Scan']", 0, 1);
        }
        else{
            client.click("NATIVE", "xpath=//*[@id='scan']", 0, 1);
        }

        client.click("NATIVE", "xpath=//*[@id='up']", 0, 1);
        client.click("NATIVE", "xpath=//*[@class='android.widget.ImageView' and ./parent::*[@id='home_page_ink_finder']]", 0, 1);
        client.click("NATIVE", "xpath=//*[@id='inktoner_enter_cartridge']", 0, 1);
        client.sendText("hp red ink cartridge");
        client.click("NATIVE", "xpath=//*[@text='FIND']", 0, 1);
        if(client.swipeWhileNotFound("Down", 0, 2000, "NATIVE", "xpath=//*[@text='Office Depot® Brand OD02PM (HP 02) Remanufactured High-Yield Light Magenta Ink Cartridge']", 0, 1000, 5, true)){
            // If statement
        }
        client.verifyElementNotFound("NATIVE", "xpath=//*[@id='actionbar_notifcation_textview']", 0);
        client.click("NATIVE", "xpath=//*[@text='Add to Cart']", 0, 1);
        client.verifyElementFound("NATIVE", "xpath=//*[@id='actionbar_notifcation_textview']", 0);
        client.click("NATIVE", "xpath=//*[@id='actionbar_notifcation_textview']", 0, 1);
        client.click("NATIVE", "xpath=//*[@id='cart_v2_coupon_code']", 0, 1);
        client.sendText("40222");
        client.click("NATIVE", "xpath=//*[@id='cart_v2_apply_coupon']", 0, 1);
        client.verifyElementFound("NATIVE", "xpath=//*[@text='Error']", 0);
        client.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
        client.click("NATIVE", "xpath=//*[@id='cart_v2_row_menu_button']", 0, 1);
        client.click("NATIVE", "xpath=//*[@text='Buy Later']", 0, 1);

        client.closeKeyboard();

        client.click("NATIVE", "xpath=//*[@id='home']", 0, 1);
        if(client.swipeWhileNotFound("Down", 0, 2000, "NATIVE", "xpath=//*[@text='FIND STORE']", 0, 1000, 5, true)){
            // If statement
        }
        if(client.applicationClose("com.officedepot.mobile.ui")){
            // If statement
        }
    }

    @Override
    protected void IOSRunTest() {

    }
}
