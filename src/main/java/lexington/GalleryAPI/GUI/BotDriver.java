package lexington.GalleryAPI.GUI;

import lexington.GalleryAPI.Objects.Article;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BotDriver {
    public ChromeDriver driver;

    public void InitailiseDriver(){
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            Map<String, String> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", "Nexus 5");
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("mobileEmulation", mobileEmulation);
            HashMap<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.managed_default_content_settings.images", 2);
            options.setExperimentalOption("prefs", prefs);
            driver = new ChromeDriver(options);

    }

    public void LoadPage(String url){
        driver.get(url);
    }

    public void Quit(){
        driver.quit();
    }
    public void MoveToComment() {
        //Move to comment
        WebElement memo = driver.findElement(By.id("comment_memo"));
        memo.sendKeys(" ");
        WebElement element2 = driver.findElement(By.cssSelector("button[class=btn-comment-write]"));
        Actions actions2 = new Actions(driver);
        actions2.moveToElement(element2).perform();
    }

    public void RemoveNotifications() {
        while (true) {
            try {
                Thread.sleep(50);
                driver.switchTo().alert().accept();
            } catch (Exception e) {
                break;
            }
        }
    }
    public abstract void DoMyAction(ArrayList<Article> articles);

}
