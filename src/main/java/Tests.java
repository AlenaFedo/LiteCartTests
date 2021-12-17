import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import java.util.List;

public class Tests extends MainClass{

    @Test
    public void loginTest() {
        driver.get("http://localhost/litecart/admin/");
        Login();
    }

    @Test
    public void menuTest() {
        driver.get("http://localhost/litecart/admin/");
        Login();

        List<WebElement> list = driver.findElements(By.cssSelector("li#app-"));

        for ( int i=1; i<=list.size(); i++){
            var element = driver.findElement(By.cssSelector("ul#box-apps-menu>  li:nth-child("+i+")"));
            element.click();
            System.out.println("Page has header "+GetHeader());
            var size = driver.findElements(By.cssSelector("ul.docs li")).size();
            for ( int j=1; j<=size; j++)
            {
                var subelement = driver.findElement(By.cssSelector("ul.docs li:nth-child("+j+")"));
                        subelement.click();
                System.out.println("   SubHeader: "+GetHeader());
            }
         }
    }

    @Test
    public void stikersVerificationTest() {

        driver.get("http://localhost/litecart/");


        List<WebElement> list = driver.findElements(By.cssSelector("ul.listing-wrapper.products li"));

        for ( WebElement element : list){
            var name = element.findElement(By.cssSelector("div.name"));
            try {
                var stiker = element.findElement(By.cssSelector("div[class*=stiker]"));
                System.out.println("Name " + name +": Stiker " + stiker );
            }
            catch (NoSuchElementException ex)
            {
                System.out.println("Element " + name + "Stiker wasn't fould ");
            }

            System.out.println("Page has header "+GetHeader());

        }
    }

    private static void Login()
    {
        WebElement username = driver.findElement(By.xpath("//input[@name=\"username\" ]"));
        username.sendKeys("admin");
        driver.findElement(By.xpath("//input[@name=\"password\" ]")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@name=\"login\" ]")).click();
    }

    private String GetHeader()
    {
        List<WebElement> list = driver.findElements(By.cssSelector("h1"));
        if(list.size()>0)
        {
            return list.get(0).getText();
        }
        return null;
    }
}
