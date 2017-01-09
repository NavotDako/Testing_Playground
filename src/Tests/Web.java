package Tests;

import static org.junit.Assert.fail;

import FrameWork.AbsTest;
import FrameWork.Command;

import java.util.Map;


public class Web extends AbsTest {
	
	public Web(String deviceToTest, String deviceQuery, String testName, Map<String, Command> commandMap) {
		super(deviceToTest, deviceQuery, testName, commandMap);
	}

	@Override
	protected void AndroidRunTest() {
		final String searchBox = "xpath=//*[(@id='kw' and @name='_nkw') or @id='gh-ac-box2']";
		final String searchButton = "xpath=//*[@id='searchTxtBtn' or @id='gh-btn' or @id='ghs-submit']";
		final String tabElement = "xpath=//*[@class='srp-item__title' or @class='grVwBg' or @class='s-item']";

		client.launch("chrome:m.ebay.com", true, true);
		client.hybridWaitForPageLoad(30000);
		client.waitForElement("WEB", searchBox, 0, 30000);
		client.elementSendText("WEB", searchBox, 0, "Hello");
		client.click("WEB", searchButton, 0, 1);
		client.verifyElementFound("WEB", tabElement, 0);
		try {
			client.getAllValues("WEB", tabElement, "text");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@");
			client.getAllValues("WEB", tabElement, "text");
		}
		client.elementSendText("WEB", searchBox, 0, "Hello Again");
		client.click("WEB", searchButton, 0, 1);
		client.waitForElement("WEB", tabElement, 0, 20000);
		client.verifyElementFound("WEB", tabElement, 0);
		client.elementSendText("WEB", searchBox, 0, "3rd Time... Enough Already!");
		String searchText = client.elementGetProperty("WEB", searchBox, 0, "value");
		System.out.println(searchText);
		client.click("WEB", searchButton, 0, 1);
		client.waitForElement("WEB", tabElement, 0, 20000);
		client.verifyElementFound("WEB", tabElement, 0);
	}

	@Override
	protected void IOSRunTest() {
		String search = "xpath=//*[@text='About Wikipedia' and @top='true' or @id='searchInput']";
		try {
			client.launch("safari:m.ebay.com", true, true);
		}catch(Exception e){
			if(client.isElementFound("Native","xpath=//*[@text='Submit']",0)){
				client.click("Native","xpath=//*[@text='Cancel' and ./parent::*[./following-sibling::*[@class='UIAView' and ./*[@class='UIAView']]]]",0,1);
				client.launch("safari:m.ebay.com", true, true);
			}
		}

			client.hybridWaitForPageLoad(30000);
			client.launch("safari:http://wikipedia.org", true, false);
		if(client.waitForElement("WEB", "xpath=//*[@id='searchInput']", 0, 15000)){
			client.elementSendText("WEB", "xpath=//*[@id='searchInput']", 0, "Long Run");
		}
		else{
			System.err.println("No Internet Connection!");
			//fail();
		}
		client.click("WEB", "xpath=//*[@nodeName='BUTTON']", 0, 1);
		client.waitForElement("WEB", "xpath=//*[@id='mw-mf-main-menu-button']", 0, 30000);
		client.click("WEB","xpath=//*[@id='mw-mf-main-menu-button']",0,1);
		client.sleep(1000);
		client.click("WEB", "xpath=//*[@class='transparent-shield cloaked-element']", 0, 1);
		client.waitForElementToVanish("WEB", "xpath=//*[@text='About Wikipedia' and @top='true']", 0, 30000);
		client.click("WEB", search, 0, 1);
		client.syncElements(2000, 15000);
		try{
			client.sendText("LONG RUN{ENTER}");
		}catch(Exception e ){
			System.out.println("**********FALIED TO SEND TEXT**************");
			e.printStackTrace();
			client.sendText("LONG RUN{ENTER}");
		}
		client.syncElements(2000, 30000);
		client.waitForElement("WEB", "xpath=//*[@id='ca-edit']", 0, 30000);
		String[] str0 = client.getAllValues("WEB", "xpath=//*[@id='ca-edit']", "hidden");

		if(str0[0].equals("false")){
			client.click("WEB", "id=ca-edit", 0, 1);
			client.swipeWhileNotFound("DOWN", 250, 1000, "web","xpath=//*[@text='Sign up']"
					,0 ,0,2 , true);
			client.syncElements(1000, 20000);
			client.sendText("{LANDSCAPE}");
			client.getTextIn("WEB", "xpath=/*//*[@id='section_0']", 0, "WEB", "Inside", 0, 0);
			client.elementSendText("WEB", "xpath=/*//*[contains(@id,'wpName')]", 0, "LONG");
			client.elementSendText("WEB", "id=wpPassword2", 0, "RUN");
			client.elementSendText("WEB", "id=wpRetype", 0, "123456");
			client.closeKeyboard();
			client.swipeWhileNotFound("DOWN", 250, 1000, "WEB","id=wpEmail" ,0 , 0, 2, true);
			client.elementSendText("WEB", "id=wpEmail", 0, "LONG@RUN.COM");
			client.sendText("{PORTRAIT}");
			client.sendText("{CLOSEKEYBOARD}");
			client.sendText("{LANDSCAPE}");
			client.swipeWhileNotFound("DOWN", 150, 1000, "WEB","xpath=//*[@id='wpCreateaccount' and @onScreen='true']" ,0 , 0, 2, true);
			client.syncElements(1000, 20000);
			if(client.isElementFound("native","xpath=//*[@text='Save Password']",0))
				client.click("Native","xpath=//*[@text='Not Now']",0,1);
			client.verifyElementFound("WEB", "xpath=//*[@class='error']", 0);
			client.click("WEB", "xpath=//*[@id='mw-mf-main-menu-button']", 0, 1);
			client.sendText("{PORTRAIT}");
			client.syncElements(1000, 20000);
			//client.sleep(500);
			client.click("WEB", "xpath=//*[@text='Home']",0,1);

		}
		else{
			String str1 = client.getTextIn("WEB", "xpath=//*[@id='mw-mf-last-modified']", 0, "TEXT", "Inside", 0, 0);
			if(str1.contains("Last modified")){
				System.out.println("Text was identified!");
			}
			client.click("WEB", "id=mw-mf-main-menu-button", 0, 1);
			String str2 = client.getText("WEB");
			if(str2.contains("HOME")){
				client.click("WEB", "xpath=//*[@text='Home' and @nodeName='A']", 0, 1);
				client.waitForElementToVanish("WEB", "xpath=//*[@text='Watch List' and @nodeName='A']", 0, 1);
			}
		}
		client.launch("http://www.google.com",true,false);
	}



}



