package put.selenium.linear;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import put.selenium.utils.ScreenshotAndQuitOnFailureRule;

import static org.junit.Assert.*;


public class AccountsLinearScriptAT {

    private WebDriver driver;

    @Rule
    public ScreenshotAndQuitOnFailureRule screenshotOnFailureAndWebDriverQuitRule =
            new ScreenshotAndQuitOnFailureRule();


    @Before
    public void setUp() throws Exception {
        //System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Janek_PC\\Downloads\\chromedriver_win32\\chromedriver.exe");
        this.driver = new ChromeDriver();
        screenshotOnFailureAndWebDriverQuitRule.setWebDriver(driver);
        driver.get("http://localhost:8080/accounts/controller?action=db_reset");
    }

    @Test
    public void successfulUserRegistration() throws Exception {
        driver.get("http://localhost:8080/accounts/controller");
        assertEquals("Login", driver.findElement(By.xpath("//div[@id='contentSingle']/h3")).getText());
        driver.findElement(By.linkText("Register")).click();
        assertEquals("Register", driver.findElement(By.xpath("//div[@id='contentSingle']/h3")).getText());
        assertEquals("username:", driver.findElement(By.xpath("//div[@id='contentSingle']/form/table/tbody/tr/td")).getText());
        driver.findElement(By.name("username")).click();
        driver.findElement(By.name("username")).clear();
        driver.findElement(By.name("username")).sendKeys("Test");
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("test");
        driver.findElement(By.name("repeat_password")).click();
        driver.findElement(By.name("repeat_password")).clear();
        driver.findElement(By.name("repeat_password")).sendKeys("test");
        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("janek");
        driver.findElement(By.xpath("//div[@id='contentSingle']/form/table/tbody/tr[7]/td")).click();
        driver.findElement(By.name("addressData")).click();
        driver.findElement(By.name("addressData")).clear();
        driver.findElement(By.name("addressData")).sendKeys("kik@wp.pl");
        driver.findElement(By.xpath("//div[@id='contentSingle']/form/table/tbody/tr[7]/td")).click();
        driver.findElement(By.name("submit")).click();
        driver.close();
    }


}
