package lexington.GalleryAPI;

import lexington.GalleryAPI.GUI.BotDriver;
import lexington.GalleryAPI.Objects.Article;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static lexington.Main.REC_THRESHOLD;

public class RecommendBot extends BotDriver {

    public String recommendScript = "for (var i = 0; i < 5; i++) {\n" +
            "document.getElementById(\"recommend_join\").click();\n" +
            "}";

    public void DoScriptRecommend() {

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript(recommendScript);
        RemoveNotifications();
    }

    public void DoPhysicalRecommend() {
        WebElement button = driver.findElement(By.id("recommend_join"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Actions actions = new Actions(driver);
        actions.moveToElement(button).perform();
        button.click();
    }



    @Override
    public void DoMyAction( ArrayList<Article> articles) {
        for (int i = 0; i < articles.size(); i++) {
            RemoveNotifications();
            System.out.println(articles.get(i).getURL());
            if(articles.get(i).recommend >REC_THRESHOLD )continue;
            try {
                LoadPage("https://gall.dcinside.com" + articles.get(i).getURL());
                DoPhysicalRecommend();
            } catch (Exception e) {

            }
        }
        Quit();
    }


}
