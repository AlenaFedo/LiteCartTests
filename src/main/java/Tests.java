import com.google.common.base.Verify;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Tests extends MainClass{

    @Test
    public void loginTest() {
        driver.get("http://localhost/litecart/admin/");
        login();
    }

    @Test
    public void createNewProduct() {
        driver.get("http://localhost/litecart/admin/");
        login();
        File file = new File("./src/main/resources/Duck.png");
        var productName = "New Duck Product";
        createProduct(productName, "12345", "Subcategory", "Unisex",
                "55.66", "01012019", "01092025", file.getAbsolutePath());

        Assert.assertTrue(driver.findElement(By.linkText(productName)).isDisplayed());
    }


    @Test
    public void createNewUser() {
        driver.get("http://localhost/litecart/");
        String email = "user" + System.currentTimeMillis() + "@email.com";
        String password = "user";
        String firstname = "firstname";
        String lastname = "lastname";
        String address = "address";
        createUser(email, password, firstname, lastname, address);
        logout();
        login(email, password);
        logout();
    }

    @Test
    public void menuTest() {
        driver.get("http://localhost/litecart/admin/");
        login();

        List<WebElement> list = driver.findElements(By.cssSelector("li#app-"));

        for ( int i=1; i<=list.size(); i++) {
            var element = driver.findElement(By.cssSelector("ul#box-apps-menu>  li:nth-child("+i+")"));
            element.click();
            System.out.println("Page has header "+ getHeader());
            var size = driver.findElements(By.cssSelector("ul.docs li")).size();
            for ( int j=1; j<=size; j++) {
                var subelement = driver.findElement(By.cssSelector("ul.docs li:nth-child("+j+")"));
                subelement.click();
                System.out.println("   SubHeader: "+ getHeader());
            }
         }
    }

    @Test
    public void stikersVerificationTest() {

        driver.get("http://localhost/litecart/");

        List<WebElement> list = driver.findElements(By.cssSelector("ul.listing-wrapper.products li"));

        for ( WebElement element : list) {
            var name = element.findElement(By.cssSelector("div.name")).getText();
            try {
                var stickers = element.findElements(By.cssSelector("div[class*=sticker]"));
                if(stickers.size()>1) {
                    System.out.println("Product " + name +"has " + stickers.size() + " Stickers ");
                }
                var sticker = element.findElement(By.cssSelector("div[class*=sticker]"));
                System.out.println("Product '" + name +"' : Sticker " + sticker.getText() );
            }
            catch (NoSuchElementException ex) {
                System.out.println("Element " + name + "Sticker wasn't fould ");
            }
        }
    }

    @Test
    public void countriesVerificationTest() {

        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        login();

        List<WebElement> list = driver.findElements(By.cssSelector("tr.row"));

        var prevName = "";
        for ( int i=2; i<=list.size()+1; i++) {

            var element = driver.findElement(By.cssSelector("table.dataTable tr:nth-child("+i+")"));

            var countryElement = element.findElement(By.cssSelector("a"));
            var name = countryElement.getText();
            System.out.println(name);
            Assert.assertFalse("nextName " + name + "not in alphabetical order", name.compareTo(prevName) < 0);

            prevName = name;

            List<WebElement> cells = element.findElements(By.xpath("./*"));

            var zones = cells.get(5).getText();

            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();",element);

            if (Integer.parseInt(zones) > 0) {
                countryElement.click();
                Assert.assertTrue(verifyZones());
                driver.navigate().back();
            }
        }
    }

    @Test
    public void zonesVerificationTest() {

        driver.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
        login();

        List<WebElement> list = driver.findElements(By.cssSelector("tr.row"));

        var prevName = "";
        for ( int i=2; i<=list.size()+1; i++) {

            var element = driver.findElement(By.cssSelector("table.dataTable tr:nth-child(" + i + ")"));

            var countryElement = element.findElement(By.cssSelector("a"));

            var name = countryElement.getText();
            System.out.println("   "+name);

            countryElement.click();
            Assert.assertTrue(verifyZones2());
            driver.navigate().back();
        }
    }

    @Test
    public void productPageVerification() {

        driver.get("http://localhost/litecart");

        var element = driver.findElement(By.cssSelector("div#box-campaigns li:nth-child(1)"));

        var productName1 = element.findElement(By.cssSelector("div.name")).getText();
        var regularPrice1 = element.findElement(By.cssSelector("s.regular-price")).getText();
        var campaignPrice1 = element.findElement(By.cssSelector("strong.campaign-price")).getText();

        var elementregularPrice = element.findElement(By.cssSelector("s.regular-price"));
        verifyFont(elementregularPrice, "regular");

        var elementcampaignPrice = element.findElement(By.cssSelector("strong.campaign-price"));
        verifyFont(elementcampaignPrice, "campaign");

        verifyFontSizes(elementregularPrice, elementcampaignPrice);

        element.findElement(By.cssSelector("a.link")).click();

        elementregularPrice = driver.findElement(By.cssSelector("s.regular-price"));
        verifyFont(elementregularPrice, "regular");

        elementcampaignPrice = driver.findElement(By.cssSelector("strong.campaign-price"));
        verifyFont(elementcampaignPrice, "campaign");

        verifyFontSizes(elementregularPrice, elementcampaignPrice);

        var productName2 = driver.findElement(By.cssSelector("h1.title")).getText();
        var regularPrice2 = driver.findElement(By.cssSelector("s.regular-price")).getText();
        var campaignPrice2 = driver.findElement(By.cssSelector("strong.campaign-price")).getText();

        Assert.assertEquals("Product names should be equals", productName1, productName2);
        Assert.assertEquals("Product regular-prices should be equals", regularPrice1, regularPrice2);
        Assert.assertEquals("Product campaign-prices should be equals", campaignPrice1, campaignPrice2);

        var elementPrice = driver.findElement(By.cssSelector("s.regular-price"));
        verifyFont(elementPrice, "regular");

        elementPrice = driver.findElement(By.cssSelector("strong.campaign-price"));
        verifyFont(elementPrice, "campaign");
    }


    private static void login()
    {
        WebElement username = driver.findElement(By.xpath("//input[@name=\"username\" ]"));
        username.sendKeys("admin");
        driver.findElement(By.xpath("//input[@name=\"password\" ]")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@name=\"login\" ]")).click();
    }

    private String getHeader()
    {
        List<WebElement> list = driver.findElements(By.cssSelector("h1"));
        if(list.size()>0) {
            return list.get(0).getText();
        }
        return null;
    }

    private boolean verifyZones()
    {
        List<WebElement> list = driver.findElements(By.cssSelector("input[name*='[name]']"));

        var prevzone = "";
        for(WebElement ele : list) {
            var zone = ele.getAttribute("value");

            System.out.println(" "+zone);

            if (zone.compareTo(prevzone) < 0) {
                System.out.println("nextZone " + zone + "not in alphabetical order");
                return false;
            }
        }
        return true;
    }

    private boolean verifyZones2()
    {
        List<WebElement> list = driver.findElements(By.cssSelector("table.dataTable [name*='[zone_code]'] [selected]"));

        for(WebElement ele : list) {
            var prevzone = "";
            var zone = ele.getAttribute("label");
            System.out.println(zone);

            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();",ele);

                if (zone.compareTo(prevzone) < 0) {
                    System.out.println("nextZone " + zone + "not in alphabetical order");
                    return false;
                }
                prevzone = zone;
        }
        return true;
    }

    private void verifyFont(WebElement element, String price) {

        var colorStyle = element.getCssValue("color");

        String[] numbers = colorStyle.replace("rgba(", "").replace(")", "").split(",");
        int r = Integer.parseInt(numbers[0].trim());
        int g = Integer.parseInt(numbers[1].trim());
        int b = Integer.parseInt(numbers[2].trim());

        if(price.equals("regular")) {

            var font = element.getCssValue("text-decoration");

            Assert.assertEquals("regular-price: RGB values equals", r, g);
            Assert.assertEquals("regular-price: RGB values equals", r, b);
            Assert.assertTrue("regular-price: Font  strike-through", font.contains("line-through"));
        }
        else {
            var font = element.getCssValue("font-weight");

            Assert.assertTrue("campaign-price: G==0", g==0);
            Assert.assertTrue("campaign-price: B==0",  b==0);
            Assert.assertTrue("campaign-price: Font bold", font.equals("bold") || font.equals("700"));
        }

    }

    private void verifyFontSizes(WebElement elementregularPrice, WebElement elementcampaignPrice)
    {
        var regularFontSize = elementregularPrice.getCssValue("font-size");
        var campaignFontSize = elementcampaignPrice.getCssValue("font-size");

        Assert.assertTrue("Regular font size should be more then campaign font size", regularFontSize.compareTo(campaignFontSize)<0);
    }

    private void createUser(String email, String password, String firstname, String lastname, String address) {
        driver.findElement(By.name("login_form")).findElement(By.cssSelector("a")).click();
        driver.findElement(By.name("firstname")).sendKeys(firstname);
        driver.findElement(By.name("lastname")).sendKeys(lastname);
        driver.findElement(By.name("address1")).sendKeys(address);
        driver.findElement(By.name("postcode")).sendKeys("123456");
        driver.findElement(By.name("city")).sendKeys("City");
        driver.findElement(By.cssSelector(".select2-selection__rendered")).click();
        driver.findElement(By.cssSelector("[id $= RU]")).click();
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("phone")).sendKeys("1234567");
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("confirmed_password")).sendKeys(password);
        driver.findElement(By.name("create_account")).click();
      }

    private void login(String email, String password) {
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("login")).click();
    }

    private void logout() {
        driver.findElement(By.linkText("Logout")).click();
    }

    private void createProduct(String productName, String id, String defaultCategory, String gender, String quantity,
                               String dateFrom, String dateTo, String fileName)
    {
        driver.findElement(By.xpath("//*[text()='Catalog']")).click();
        driver.findElement(By.xpath("//*[text()=' Add New Product']")).click();
        //fill General tab

        driver.findElement(By.cssSelector("input[type = radio][value = '1']")).click();
        driver.findElement(By.name("name[en]")).sendKeys(productName);
        driver.findElement(By.name("code")).sendKeys(id);
        driver.findElement(By.cssSelector("input[type = checkbox][value = '2']")).click();
        driver.findElement(By.name("default_category_id")).click();
        driver.findElement(By.xpath(String.format("//*[text()='%s']", defaultCategory))).click();
        driver.findElement(By.xpath(String.format("//tr/td[text()='%s']/../td[1]", gender))).click();
        driver.findElement(By.name("quantity")).clear();
        driver.findElement(By.name("quantity")).sendKeys(quantity);
        driver.findElement(By.cssSelector("input[type='date'][name='date_valid_from']")).sendKeys(dateFrom);
        driver.findElement(By.cssSelector("input[type='date'][name='date_valid_to']")).sendKeys(dateTo);

        WebElement fileInput = driver.findElement(By.cssSelector("input[type='file'][name='new_images[]']"));
        fileInput.sendKeys(fileName);

        //fill Information tab
        driver.findElement(By.xpath("//*[text()='Information']")).click();
        driver.findElement(By.name("manufacturer_id")).click();
        driver.findElement(By.xpath("//*[text()='ACME Corp.']")).click();
        driver.findElement(By.name("keywords")).sendKeys("New keywords");
        driver.findElement(By.name("short_description[en]")).sendKeys("short description");
        driver.findElement(By.className("trumbowyg-editor")).sendKeys("Long description");
        driver.findElement(By.name("head_title[en]")).sendKeys("Head Title");
        driver.findElement(By.name("meta_description[en]")).sendKeys("Meta description");

        //fill Prices tab
        driver.findElement(By.xpath("//*[text()='Prices']")).click();
        driver.findElement(By.name("purchase_price")).sendKeys("23.99");
        driver.findElement(By.name("purchase_price_currency_code")).click();
        driver.findElement(By.xpath("//*[text()='US Dollars']")).click();
        driver.findElement(By.name("prices[USD]")).sendKeys("12.34");
        driver.findElement(By.name("prices[EUR]")).sendKeys("67.88");

        driver.findElement(By.name("save")).click();

    }
}
