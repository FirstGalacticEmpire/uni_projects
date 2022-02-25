package put.selenium.pageobjects;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import put.selenium.pageobjects.repository.*;
import put.selenium.utils.ScreenshotAndQuitOnFailureRule;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class AccountsPageObjectAT {

    private WebDriver driver;

    private ResetDatabase resetDatabse;
    private MainMenu mainMenu;
    private RegistrationPage registrationPage;
    private LoginPage loginPage;
    private MainPage mainPage;
    private LoggedInUserInformation loggedInUserInfo;

    private String hostURL;

    @Rule
    public ScreenshotAndQuitOnFailureRule screenshotOnFailureAndWebDriverQuitRule =
            new ScreenshotAndQuitOnFailureRule();


    @Before
    public void setUp() throws Exception {

        Properties properties = new Properties();
        InputStream in = getClass().getResourceAsStream("selenium.properties");
        properties.load(in);
        in.close();

        this.hostURL=properties.getProperty("host.url");

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Janek_PC\\Downloads\\chromedriver_win32\\chromedriver.exe");
        this.driver = new ChromeDriver();
        this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        screenshotOnFailureAndWebDriverQuitRule.setWebDriver(driver);

        resetDatabse = new ResetDatabase(driver, this.hostURL);
        mainMenu = new MainMenu(driver);
        registrationPage = new RegistrationPage(driver);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver, this.hostURL);
        loggedInUserInfo = new LoggedInUserInformation(driver);

        resetDatabse.resetDatabase();
    }

    @Test
    public void successfulUserRegistration() throws Exception {
        driver.get("http://localhost:8080/accounts/controller");
        //check 1
        assertEquals(driver.getCurrentUrl(), "http://localhost:8080/accounts/controller");
        driver.findElement(By.linkText("Register")).click();
        //check 2
        assertEquals("Register", driver.findElement(By.xpath("//div[@id='contentSingle']/h3")).getText());
        assertEquals("username:", driver.findElement(By.xpath("//div[@id='contentSingle']/form/table/tbody/tr/td")).getText());
        driver.findElement(By.name("username")).sendKeys("user");
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.name("repeat_password")).sendKeys("password");
        driver.findElement(By.name("name")).sendKeys("John Doe");
        driver.findElement(By.name("addressData")).sendKeys("10 New St.");
        driver.findElement(By.name("submit")).click();
        //check 3
        assertEquals("Login", driver.findElement(By.xpath("//div[@id='contentSingle']/h3")).getText());
        driver.findElement(By.name("username")).click();
        driver.findElement(By.name("username")).clear();
        driver.findElement(By.name("username")).sendKeys("user");
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.name("submit")).click();
        //check 4
        assertEquals("John Doe, 10 New St.", driver.findElement(By.xpath("//div[@id='container']/div[2]")).getText());

    }


}
