package Tests;



import FrameWork.BaseTest;
import FrameWork.Command;

import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 */
public class Authentication extends BaseTest {

    public Authentication(String deviceToTest,String deviceQuery, String testName, Map<String, Command> commandMap) {
        super(deviceToTest, deviceQuery ,testName, commandMap);
    }

    private String[] replies={
            "Success" ,
            "StopMock",
            "AuthenticationFailedError",
            "UserCancelError",
            "UserFallbackError",
            "SystemCancelError",
            "PasscodeNotSetError",
            "TouchIDNotAvailableError",
            "TouchIDNotEnrolledError",
            "TouchIDLockoutError",
            "AppCancelError",
            "InvalidContextError"};

    private String[] expectedReplies={
            "Success",
            "Error Code: -7. No fingers are enrolled with Touch ID.",
            "Error Code: -1. Application retry limit exceeded.",
            "Error Code: -2. Canceled by user.",
            "Error Code: -3. Fallback authentication mechanism selected.",
            "Error Code: -4. UI canceled by system.",
            "Error Code: -5. Passcode not set.",
            "Error Code: -6. Biometry is not available on this deviceName.",
            "Error Code: -7. No fingers are enrolled with Touch ID.",
            "Error Code: -8. Biometry is locked out.",
            "Error Code: -9. kLAErrorAppCancel",
            "Error Code: -10. kLAErrorInvalidContext"};
    private int[] waitTimes = {0,1000,2000,5000,7000,8000,10000,13000,15000,17000,20000};

    @Override
    protected void AndroidRunTest() {
        //Assert.assertFalse(true);
    }

    @Override
    protected void IOSRunTest() {
        client.install("http://192.168.2.72:8181/iOSApps/UICatalog.ipa",true,true);
        client.launch("com.experitest.UICatalog", true, true);
        client.elementListSelect("", "text=Authentication", 0, true);
        String uIReply;
        String reply;
        for (int i=0;i<replies.length;i++){
            reply=replies[i];
            client.startStepsGroup(reply);
            client.setAuthenticationReply(reply, 0);
            client.click("NATIVE", "text=Request Touch ID Authentication", 0, 1);
            uIReply = client.elementGetText("NATIVE", "xpath=//*[@class='UIView' and @height>0 and ./*[@text='TouchID']]//*[@class='UILabel']", 1);
            boolean correctResult=uIReply.equals(expectedReplies[i]);
            if (correctResult){
                client.report("Correct Result", true);
            }
            else{
                client.report("Incorrect Result, should be "+expectedReplies[i], false);
            }
            assertTrue(correctResult);
            client.click("NATIVE", "xpath=//*[@class='_UIAlertControllerActionView']/*/*", 0, 1);
            client.stopStepsGroup();
        }
        client.uninstall("com.experitest.UICatalog");
    }
}
