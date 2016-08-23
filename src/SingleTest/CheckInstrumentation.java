package SingleTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.notification.Failure;

import com.experitest.client.Client;

public class CheckInstrumentation {

	private String host = "localhost";
	private int port = 8889;
	private static String projectBaseDirectory;// = "C:\\Users\\richi.lebovich\\workspace\\project2";
	protected Client client = null;
	private static String global_instrumentation_version;
	private String model = "";
	private static String run_millis_path;
	private static String summary_file_path;
	private  String  device_path;
	private  String test_path;
	private static String device_name;
	private static int total_number_of_tests = 12;
	private static int current_test_number = 0;
	private static Map<String, String> errors;

	private static String apps_directory = "http://192.168.2.72:8181/iOSApps/";
	private static String keyle_path = "C:\\Program Files (x86)\\Experitest\\SeeTest 9.8";

	@BeforeClass
	public static void getInstrumentationVersion(){

		try {
			projectBaseDirectory = createProjectBaseDirectory();

			errors = new HashMap<>();
			System.out.println("keyle_path is: " + keyle_path);
			Process process = new ProcessBuilder(keyle_path + "\\keyle.exe","info","-app", ".//bin\\ipas\\EriBank.ipa").start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				if(line.contains("Bundle Version")){
					global_instrumentation_version = line.substring(line.length()-4,line.length() );
					System.out.println("global_instrumentation_version is: " + global_instrumentation_version );
				}
			}

		} catch (IOException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}

		run_millis_path = createRunMillisFolder(projectBaseDirectory);
		summary_file_path = createSummaryFile(run_millis_path);
	}

	@Rule
	public TestName test_name = new TestName();

	@Before
	public void setUp(){
		current_test_number++;
		System.out.println("Starting 'before' method on Memory number " + current_test_number + " from " + total_number_of_tests);
		client = new Client(host, port, true);
		client.setProjectBaseDirectory(projectBaseDirectory);
		client.waitForDevice("@os=\"ios\"", 300000);

		String str3 = client.getDeviceProperty("device.model");
		System.out.println("str3 is: " + str3);
		if(str3.contains("iPad")){
			model = "iPad";
		}
		else if (str3.contains("iPhone")){
			model = "iPhone";
		}
		System.out.println("model is: " + model);

		device_name = client.getDeviceProperty("device.name");
		device_path = createDeviceFolder(device_name, run_millis_path);
		System.out.println("device_path is: " + device_path);
		String current_test_name = test_name.getMethodName();
		test_path = createNewTestFolder(device_path, current_test_name);
		System.out.println("test_path is: " + test_path);
		System.out.println("Installing app: " + current_test_name.substring(21));


		client.setReporter("xml", test_path, test_name.getMethodName());

	}

	@Test
	public void testCheckInstrumentedEriBank(){
		try{
			client.install(apps_directory + "\\EriBankO.ipa", true, true);
			System.out.println("eribank installed successfully");
			client.launch("com.experitest.ExperiBankO", true, true);
			checkInstrumentationVersionMatch();
			client.click("NATIVE", "xpath=//*[@text='Login']", 0, 1);
			client.verifyElementFound("NATIVE", "xpath=//*[@text='Dismiss']", 0);
			client.uninstall("com.experitest.ExperiBankO");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			Assert.assertFalse(false);
			throw e;
		}
	}

	@Test
	public void testCheckInstrumentedfmr_ios() {
		try{
			Assert.assertTrue("The device model for this Memory - testCheckInstrumentedfmr_ios -  must be 'iphone'. but instead it's: " + model, model.equals("iPhone"));
			client.install(apps_directory + "\\fmr_ios.ipa", true, true);
			client.launch("com.fidelity.watchlist-AdHoc", true, true);
			client.click("NATIVE", "nixpath=//*[@text='Don’t Allow' and @knownSuperClass='_UIAlertControllerActionView' and @enabled='true']", 0, 1);
			checkInstrumentationVersionMatch();
			client.click("NATIVE", "xpath=//*[@text='Agree']", 0, 1);
			client.verifyElementFound("NATIVE", "xpath=//*[@class='UITabBarSwappableImageView' and ./parent::*[@accessibilityLabel='Feed']]", 0);
			client.uninstall("com.fidelity.watchlist-AdHoc");
		}
		catch (AssertionError | Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			throw e;
		}
	}

	@Test//(expected=RuntimeException.class)
	public void testCheckInstrumentedicbc(){
		try{
			client.install(apps_directory + "\\icbc.ipa", true, true);
			client.launch("cn.com.icbc.mobileOA", true, true);
			checkInstrumentationVersionMatch();
			client.click("NATIVE", "xpath=//*[@text='RemoveAll']", 0, 1);
			client.isElementBlank("NATIVE", "xpath=//*[@accessibilityLabel='http://82.201.45.189:10795/']", 0, 10);

			client.uninstall("cn.com.icbc.mobileOA");

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			Assert.assertFalse(false);
			throw e;
		}
	}

	@Test
	public void testCheckInstrumentedsap(){
		try{
			Assert.assertTrue("The device model for this Memory -  testCheckInstrumentedsap - must be 'iPad'. but instead it's: " + model, model.equals("iPad"));

			client.install(apps_directory + "\\sap.ipa", true, true);
			client.launch("com.sap.byd.cod.ipad.internal", true, true);
			checkInstrumentationVersionMatch();
			client.click("NATIVE", "xpath=//*[@text='Log On' and ./parent::*[./parent::*[@class='UITableViewCellContentView']]]", 0, 1);
			client.verifyElementFound("NATIVE", "xpath=//*[@text='Dismiss']", 0);
			client.uninstall("com.sap.byd.cod.ipad.internal");
		}
		catch (AssertionError | Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			Assert.assertFalse(false);
			throw e;
		}
	}

	@Test
	public void testCheckInstrumentedShaadi(){
		try{
			Assert.assertTrue("The device model for this Memory - "+test_name.getMethodName()+"- must be 'iphone'. but instead it's: " + model, model.equals("iPhone"));

			client.install(apps_directory + "\\Shaadi.ipa", true, true);
			client.launch("com.shaadi.iphone", true, true);
			checkInstrumentationVersionMatch();
			client.click("NATIVE", "xpath=//*[@text='Register Free']", 0, 1);
			client.verifyElementFound("WEB", "xpath=//*[@text='FortiGuard Web Filtering']", 0);
			client.uninstall("com.shaadi.iphone");
		}
		catch (AssertionError | Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			Assert.assertFalse(false);
			throw e;
		}

	}

	@Test
	public void testCheckInstrumentedUICatalog(){
		try
		{
			client.install(apps_directory + "\\UICatalog.ipa", true, true);
			client.launch("com.experitest.UICatalog", true, true);
			checkInstrumentationVersionMatch();
			client.click("NATIVE", "xpath=//*[@accessibilityLabel='Buttons']", 0, 1);
			client.verifyElementFound("NATIVE", "xpath=(//*[@class='UITableView']/*/*/*[@text='UIBUTTON' and @class='_UITableViewHeaderFooterViewLabel'])[1]", 0);
			client.uninstall("com.experitest.UICatalog");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			Assert.assertFalse(false);
			throw e;
		}

	}

	@Test
	public void testCheckInstrumentedSamplecode(){
		try{
			Assert.assertTrue("The device model for this Memory - "+test_name.getMethodName()+"- must be 'iphone'. but instead it's: " + model, model.equals("iPhone"));

			client.install(apps_directory + "\\samplecode.ipa", true, true);
			client.launch("com.apple.samplecode.ViewControllerPreview", true, true);
			client.click("NATIVE", "nixpath=//*[@text='OK']", 0, 1);
			checkInstrumentationVersionMatch();
			client.click("NATIVE", "xpath=//*[@text='ExperiTest']", 0, 1);
			client.verifyElementFound("NATIVE", "nixpath=//*[@text='ExperiTest']", 0);
			client.uninstall("com.apple.samplecode.ViewControllerPreview");
		}
		catch (AssertionError | Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			Assert.assertFalse(false);
			throw e;
		}
	}

	@Test
	public void testCheckInstrumentedWKBrowser(){
		try{
			client.install(apps_directory + "\\WKBrowser.ipa", true, true);
			client.launch("com.experitest.WKBrowser", true, true);
			checkInstrumentationVersionMatch();
			client.click("NATIVE", "xpath=//*[@text='Go']", 0, 1);
			client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='http://']", 0);
			client.uninstall("com.experitest.WKBrowser");

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			Assert.assertFalse(false);
			throw e;
		}
	}

	@Test
	public void testCheckInstrumentedQuickContactsTest(){
		try{
			client.install(apps_directory + "\\QuickContactsTest.ipa", true, true);
			client.launch("com.experitest.QuickContacts", true, true);
			checkInstrumentationVersionMatch();
			client.click("NATIVE", "xpath=//*[@accessibilityLabel='Edit Unknown Contact']", 0, 1);
			client.verifyElementFound("NATIVE", "xpath=//*[@text='Quick Contacts']", 0);
			client.uninstall("com.experitest.QuickContacts");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			Assert.assertFalse(false);
			throw e;
		}
	}

	@Test
	public void testCheckInstrumentediOSBridge(){
		try{
			client.install(apps_directory + "\\iOSBridge.ipa", true, true);
			client.launch("com.experitest.LaunchAgent", true, true);
			checkInstrumentationVersionMatch();
			client.verifyElementFound("NATIVE", "nixpath=//*[@text='iOSBridge:']", 0);
			client.uninstall("com.experitest.LaunchAgent");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			Assert.assertFalse(false);
			throw e;
		}
	}

	@Test
	public void testCheckInstrumentedQuickContacts(){
		try{
			client.install(apps_directory + "\\QuickContacts.ipa", true, true);
			client.launch("com.experitest.QuickContacts", true, true);
			checkInstrumentationVersionMatch();
			client.click("NATIVE", "xpath=//*[@accessibilityLabel='Edit Unknown Contact']", 0, 1);
			client.verifyElementFound("NATIVE", "xpath=//*[@text='Quick Contacts']", 0);
			client.uninstall("com.experitest.QuickContacts");

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			Assert.assertFalse(false);
			throw e;
		}
	}

	@Test
	public void testCheckInstrumentedBrowser(){
		try{
			client.install(apps_directory + "\\Browser.ipa", true, true);
			client.launch("com.experitest.Browser", true, true);
			checkInstrumentationVersionMatch();
			client.click("NATIVE", "xpath=//*[@text='Go']", 0, 1);
			client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='http://']", 0);
			client.uninstall("com.experitest.Browser");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			addError(e.getMessage(), test_name.getMethodName());
			Assert.assertFalse(false);
			throw e;
		}
	}

	@After
	public void tearDown(){
		System.out.println("finished " + test_name.getMethodName());
		// Generates a report of the Memory case.
		// For more information - https://docs.experitest.com/display/public/SA/Report+Of+Executed+Test
		client.generateReport(false);
		// Releases the client so that other clients can approach the agent in the near future.
		client.releaseClient();
		System.out.println("Finished 'after' method on Memory number " + current_test_number + " from " + total_number_of_tests);
	}

	@AfterClass
	public static void fillSummaryFile(){
		fillSummaryFile(device_name, summary_file_path, errors);
	}

	public void checkInstrumentationVersionMatch(){
		String current_instrumentation_version = client.getDeviceProperty("instrumentation.version");
		System.out.println("current_instrumentation_version is: " + current_instrumentation_version);
		System.out.println("global_instrumentation_version is: " + global_instrumentation_version);
		String Instrumentation_version_mismatch_error =
				"current_instrumentation_version is: " + current_instrumentation_version +
						" while global_instrumentation_version is: " + global_instrumentation_version;

		try{

			Assert.assertTrue(Instrumentation_version_mismatch_error,current_instrumentation_version.contains(global_instrumentation_version));
		}
		catch(AssertionError e){
			System.out.println(e.getMessage());
			System.out.println(Instrumentation_version_mismatch_error);
			addError(e.getMessage(), test_name.getMethodName());
		}
	}

	public static void addError(String e, String testName){
		System.out.println("addError called");
		errors.put(testName, e);
	}

	public static String createDeviceFolder(String device_name, String run_miliis_folder){
		device_name = device_name.replace(":", "_");
		String device_folder_path = run_miliis_folder + "\\" + device_name;
		new File(device_folder_path).mkdirs();
		return device_folder_path;
	}

	public static String createNewTestFolder(String device_path, String test_name){
		String new_path = device_path + "\\" + test_name;
		new File(new_path).mkdirs();
		System.out.println("test_path: " +new_path+" created");
		return new_path;
	}

	public static String createProjectBaseDirectory(){
		String path = System.getProperty("user.dir");
		System.out.println("The ProjectBaseDirectory is: " + path);
//		String new_path = path+"\\RUN_"+System.currentTimeMillis();
//		new File(new_path).mkdirs();
//		System.out.println("path: " +new_path+" created");
		return path;
	}

	public static String createRunMillisFolder(String projectBaseDirectory){
		String path = projectBaseDirectory;//System.getProperty("user.dir");
		System.out.println("The path is: " + path);
		String new_path = path+"\\RUN_"+System.currentTimeMillis();
		new File(new_path).mkdirs();
		System.out.println("path: " +new_path+" created");
		return new_path;
	}

	public static String createSummaryFile(String run_path){
		String summary_file_path = run_path + "\\OverallSummaryView.csv";
		File newFile = new File(summary_file_path);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return summary_file_path;
	}

	public static void fillSummaryFile(String device_name, String summary_file_path,
									   Map<String,String> all_errors){
		try {
			File summaryFile = new File(summary_file_path);
			BufferedWriter writer = new BufferedWriter(new FileWriter(summaryFile));
			writer.write("Device Name, Test Name, Exception");
			writer.newLine();

			for(String test_name : all_errors.keySet()){
				System.out.println("device_name: " + device_name + " wrote into file");
				System.out.println("test_name: " + test_name + " wrote into file");
				System.out.println("error name: " + all_errors.get(test_name) + " wrote into file");
				writer.write(device_name + "," + test_name + "," + all_errors.get(test_name));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}

