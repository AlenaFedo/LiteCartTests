import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;


public class MainClass {

    public static WebDriver driver;

    @Before
    public void start() {

        //driver = StartFirefox();
        driver = startChrome();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        //driver = StartOpera();
        //driver = StartInternetExplorer();
    }


private static ChromeDriver startChrome()
{
    System.setProperty("webdriver.chrome.driver",
        "D:\\Selenium\\Drivers\\chromedriver.exe");

    ChromeOptions options = new ChromeOptions();
    options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");

    options.addArguments("start-maximized");
    var driver = new ChromeDriver(options);
    return driver;
}

    private static FirefoxDriver startFirefox()
    {
        System.setProperty("webdriver.gecko.driver",
                "D:\\Selenium\\Drivers\\geckodriver.exe");
        var driver = new FirefoxDriver();

        driver.manage().window().maximize();
        return driver;
    }

    private static OperaDriver startOpera()
    {
        System.setProperty("webdriver.opera.driver",
                "D:\\Selenium\\Drivers\\operadriver.exe");
        var driver = new OperaDriver();

        driver.manage().window().maximize();
        return driver;
    }

    private static InternetExplorerDriver startInternetExplorer()
    {
        System.setProperty("webdriver.ie.driver",
                "D:\\Selenium\\Drivers\\IEDriverServer.exe");

        var driver = new InternetExplorerDriver();

        driver.manage().window().maximize();
        return driver;
    }

    //@After
    public void stop()
    {
    driver.quit();
    driver = null;
    }
}
