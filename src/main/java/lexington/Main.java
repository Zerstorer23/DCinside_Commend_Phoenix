package lexington;

import lexington.GalleryAPI.Crawler;
import lexington.GalleryAPI.Crawler_title;
import lexington.GalleryAPI.GUI.mainScene;
import lexington.GalleryAPI.Gallery;
import lexington.GalleryAPI.Objects.Article;
import lexington.GalleryAPI.RecommendBot;
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
    public static final int TITLES_IN_PAGE = 50;
    public static  final int REC_THRESHOLD = 10;
    public static  final int MAX_PAGE_NUMBER = 1;
    public static final String GALL_ID = "lilyfever";
    public static  final boolean IS_MAJOR = false;
    public static ArrayList<Article> static_articles;

    public static mainScene mc;

    public static void main(String[] args) throws IOException, InterruptedException {
        InitialiseUI();
        DoKotlinRecommend();

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
    public static void InitialiseUI(){
        mc = new mainScene();
        mc.initUI();

    }
    public static void DoRecommendBot() throws InterruptedException {
        Gallery gall = new Gallery();
        gall.setInfo(GALL_ID, IS_MAJOR);
        Crawler crawler = new Crawler_title(mc, gall, 1, MAX_PAGE_NUMBER, 1);
        crawler.scrollRaw();

        ArrayList<Article> articles = crawler.getArticles();
        System.out.println(articles.size());
        boolean userDriver = false;
        if (userDriver) {
            RecommendBot bot = new RecommendBot();
            bot.InitailiseDriver();
            bot.DoMyAction(articles);
        } else {

        }


    }
    public static void DoKotlinRecommend()throws InterruptedException {
        Gallery gall = new Gallery();
        gall.setInfo(GALL_ID, IS_MAJOR);
        Crawler crawler = new Crawler_title(mc, gall, 1, MAX_PAGE_NUMBER, 1);
        crawler.scrollRaw();
        ArrayList<Article> articles = crawler.getArticles();

        int i =0;
        for (Article article : articles) {
            if(article.recommend >= REC_THRESHOLD) continue;
            int articleId = article.id;
            KotlinTest kt = new KotlinTest();
            kt.initKotlinInside();
            kt.testArticleVote(GALL_ID,articleId);
            Thread.sleep(10);
        }

    }





}
