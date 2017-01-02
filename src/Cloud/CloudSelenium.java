package Cloud;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class CloudSelenium {

    static WebDriver driver = new ChromeDriver();

    public static void main(String[] args) throws InterruptedException {
        String SERVER = "192.168.2.13";
        int PORT  =8090;
        String serverURL= "http://"+SERVER+":"+PORT;
        String user= "admin";
        String password = "Experitest2012";

        String appPath = "C:\\Users\\DELL\\EclipseWorkspace\\Testing Playground STA\\lib\\EriBankO.ipa";

        System.setProperty("webdriver.chrome.driver","C:\\Users\\DELL\\Downloads\\chromedriver\\chromedriver.exe");

        Login(serverURL,user,password);
        AddApp(serverURL,appPath);

    }

    private static void Login(String serverURL, String user, String password) throws InterruptedException {
        driver.get(serverURL);
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("body > div.ng-scope > div.middle-box.text-center.loginscreen.animated.fadeIn.ng-scope > div > form > div:nth-child(1) > div.input-group > input")).sendKeys(user);
        driver.findElement(By.cssSelector("body > div.ng-scope > div.middle-box.text-center.loginscreen.animated.fadeIn.ng-scope > div > form > div:nth-child(2) > div > input")).sendKeys(password);
        driver.findElement(By.cssSelector("body > div.ng-scope > div.middle-box.text-center.loginscreen.animated.fadeIn.ng-scope > div > form > button")).click();
        Thread.sleep(5000);
    }

    private static void AddApp(String serverURL, String appPath) throws InterruptedException {

        driver.get(serverURL+"/index.html#/applications");
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("#full-page-container > div.col-lg-12.toolbar-container > div > div > div > button:nth-child(1) > span > span")).click();
        WebElement fileInput = driver.findElement(By.cssSelector("body > div.modal.fade.ng-isolate-scope.in > div > div > div > form > div.modal-body > div:nth-child(1) > input"));
        fileInput.sendKeys(appPath);
        driver.findElement(By.cssSelector("body > div.modal.fade.ng-isolate-scope.in > div > div > div > form > div.modal-footer > button.btn.btn-primary")).click();

    }


}
