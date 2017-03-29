package Tests;

import FrameWork.BaseTest;
import FrameWork.Command;

import java.util.Map;


public class AmitApp extends BaseTest {

	public AmitApp(String deviceToTest, String deviceQuery, String testName, Map<String, Command> commandMap) {
		super(deviceToTest, deviceQuery, testName, commandMap);
	}

	@Override
	protected void AndroidRunTest() {
		client.install("http://192.168.2.72:8181/AndroidApps/Conquest%20Game%20Android.apk",true,false);
		client.launch("com.amitlicht.experitest.conquestgame/.StartActivity",true,true);

		client.elementSendText("Native","xpath=//*[@id='edit_username']",0,"BOB");
		client.elementSendText("Native","//*[@id='edit_passward']",0,"Not BOB");
		client.click("Native","//*[@text='Log In']",0,1);
		client.verifyElementFound("native","//*[@id='login_error_label']",0);
		client.elementSendText("Native","xpath=//*[@id='edit_username']",0,"BOB");
		client.elementSendText("Native","//*[@id='edit_passward']",0,"BOB");
		client.click("Native","//*[@text='Log In']",0,1);
		client.verifyElementFound("native","//*[@id='textView_wellcome']",0);
		client.uninstall("com.amitlicht.experitest.conquestgame");
	}

	@Override
	protected void IOSRunTest() {
		client.install("http://192.168.2.72:8181/iOSApps/Monster%20Island%20Game.ipa",true,false);
		client.launch("com.Experitest.AmitLicht.Monster-Island-Game",true,true);
		client.elementSendText("Native","xpath=//*[@placeholder='Enter user name']",0,"BOB");
		client.elementSendText("Native","//*[@placeholder='enter password']",0,"Not BOB");
		client.click("Native","//*[@accessibilityLabel='Login' and @class='UIButtonLabel']",0,1);
		client.verifyElementFound("native","//*[@text='Wrong user name or password.']",0);

		client.elementSendText("Native","xpath=//*[@placeholder='Enter user name']",0,"bob");
		client.elementSendText("Native","//*[@placeholder='enter password']",0,"bob");
		client.click("Native","//*[@accessibilityLabel='Login' and @class='UIButtonLabel']",0,1);
		client.verifyElementFound("native","//*[@text='Welcome: bob']",0);

		client.uninstall("com.Experitest.AmitLicht.Monster-Island-Game");
	}



}



