package lexington;

import lexington.GalleryAPI.Crawler;
import lexington.GalleryAPI.Crawler_title;
import lexington.GalleryAPI.GUI.mainScene;
import lexington.GalleryAPI.Gallery;
import lexington.GalleryAPI.Objects.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Main {
    public static final int TITLES_IN_PAGE = 100;
    public static  final int REC_THRESHOLD = 8;
    public static  final int MAX_PAGE_NUMBER = 1;
    public static final String GALL_ID = "nijidon";
    public static  final boolean IS_MAJOR = false;
    public static ArrayList<Article> static_articles;

    public static ChromeDriver driver;
    public static String recommendScript = "for (var i = 0; i < 5; i++) {\n" +
            "document.getElementById(\"recommend_join\").click();\n" +
            "}";

    public static void main(String[] args) throws IOException, InterruptedException {
        recommendphoenix();

       // go();
    }

    public static void go() {

        mainScene mc = new mainScene();
        mc.initUI();


        Gallery gall = new Gallery();
        gall.setInfo(GALL_ID, IS_MAJOR);

        Crawler crawler = new Crawler_title(mc, gall, 1, MAX_PAGE_NUMBER, 1);
        crawler.scrollRaw();

        static_articles = crawler.getArticles();
        for (int i = 0; i < static_articles.size(); i++) {
            final int index = i;
            new Thread()
            {
                @Override
                public void run()
                {
                    while(true) {
                            try {
                                String url = "https://gall.dcinside.com" + static_articles.get(index).getURL();
                                Jsoup.connect(url).get();
                                System.out.println(url);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                }

            }.start(); // 스레드 실행

        }


    }

    public static void recommendphoenix() throws InterruptedException {
        mainScene mc = new mainScene();
        mc.initUI();


        Gallery gall = new Gallery();

        gall.setInfo(GALL_ID, IS_MAJOR);

        Crawler crawler = new Crawler_title(mc, gall, 1, MAX_PAGE_NUMBER, 1);
        crawler.scrollRaw();

        ArrayList<Article> articles = crawler.getArticles();
        System.out.println(articles.size());
        boolean userDriver = false;
        if (userDriver) {
            InitialiseDriver();

            for (int i = 0; i < articles.size(); i++) {
                RemoveNotifications();
                System.out.println(articles.get(i).getURL());
                if(articles.get(i).recommend >REC_THRESHOLD )continue;
                try {
                    driver.get("https://gall.dcinside.com" + articles.get(i).getURL());
                  //  MoveToComment();
                    DoPhysicalRecommend();
                    //DoScriptRecommend();
                } catch (Exception e) {

                }
            }
            driver.quit();
        } else {
            int i =0;
            for (Article article : articles) {
                if(article.recommend >= REC_THRESHOLD) continue;
                if(i++>70) break;
                int articleId = article.id;
                KotlinTest kt = new KotlinTest();
                kt.initKotlinInside();
                kt.testArticleVote(GALL_ID,articleId);
                Thread.sleep(10);
            }
        }


    }

    private static void InitialiseDriver() {
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

    private static void DoScriptRecommend() {

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript(recommendScript);
        RemoveNotifications();
    }

    private static void DoPhysicalRecommend() {
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

    private static void MoveToComment() {
        //Move to comment
        WebElement memo = driver.findElement(By.id("comment_memo"));
        memo.sendKeys(" ");
        WebElement element2 = driver.findElement(By.cssSelector("button[class=btn-comment-write]"));
        Actions actions2 = new Actions(driver);
        actions2.moveToElement(element2).perform();
    }

    public static void RemoveNotifications() {
        while (true) {
            try {
                Thread.sleep(50);
                driver.switchTo().alert().accept();
            } catch (Exception e) {
                break;
            }
        }

    }


}
