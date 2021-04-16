package lexington.GalleryAPI;


import lexington.GalleryAPI.GUI.mainScene;
import lexington.GalleryAPI.Objects.Article;
import lexington.GalleryAPI.Objects.ArticleType;
import lexington.GalleryAPI.Objects.ImageInfo;
import lexington.GalleryAPI.Objects.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lexington.GalleryAPI.Objects.Hash_storage.Userlist;
import static lexington.GalleryAPI.Preprocessor.getPrinter;


public class Crawler_img extends Crawler {


    /**
     * 갤러리의 모든 글 제목을 긁어내는 코드입니다.
     * 유지보수 쟁점
     */

    ArrayList<ImageInfo> imageInfos = new ArrayList<>();

    public Crawler_img(mainScene scene, Gallery gall, int numberOfMultiThreads, int MaxPageNumber, int StepSize){
        this.gallery = gall;
        this.cores = numberOfMultiThreads;
        this.maxPageToScroll = MaxPageNumber;
        this.stepSize=StepSize;
        this.scene = scene;


    }



    ArrayList<Article> getImgArticles(ArrayList<Article> parent){
        ArrayList<Article> children = new ArrayList<>();
        for(Article article : parent){
            if(article.getType() == ArticleType.PICTURE){
                children.add(article);
            }
        }
        return children;
    }
    void initImgCores() {
        Crawler crawler = new Crawler_title(scene,gallery,cores,maxPageToScroll,1);
        crawler.scrollRaw();

        for (int i = 0; i < cores; i++) {
            Queue<String> lists = new LinkedList<>();
            CORES.add(lists);
        }

        //*******
        ArrayList<Article> targetArticles = getImgArticles(crawler.getArticles());
        for (int i = 0; i < targetArticles.size(); i++) {
            String page = "https://gall.dcinside.com" +targetArticles.get(i).getURL();
            System.out.println(page);
            int index = i % cores;
            CORES.get(index).add(page);
        }
        //**********

        core_sizes = new int[cores];
        for (int i = 0; i < cores; i++) {
            core_sizes[i] = CORES.get(i).size();
            scene.addLabel(i,core_sizes[i]);
        }
    }


    public void scrollRaw() {
        initImgCores();
        ArrayList<DownloadImageCore> activeCores = new ArrayList<>();
        for (int c = 0; c < cores; c++) {
            DownloadImageCore go = new DownloadImageCore(c);
            activeCores.add(go);
            go.start();
        }

        //wait for them to finish
        for (int c = 0; c < cores; c++) {
            try {
                activeCores.get(c).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(c+ "has finished...");
        }
        System.out.println("Scroll done!");
    }

    public void scrollOnePage(){
        Queue<String> lists = new LinkedList<>();
        CORES.add(lists);
        CORES.get(0).add(gallery.GetMainURL()+"1");


        core_sizes = new int[1];
        core_sizes[0] = 1;


        DownloadImageCore go = new DownloadImageCore(0);
        go.start();
            try {
                go.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
    public class DownloadImageCore extends Thread {
        int coreID;
        DownloadImageCore(int c){
            coreID = c;
        }

        public void run(){
            while (!CORES.get(coreID).isEmpty()) {
                String pageURL = CORES.get(coreID).poll();
                try {
                    System.out.println(pageURL);

                    //1. 게시판 body 추출
                    Document doc = Jsoup.connect(pageURL).get();
                    Element body = doc.select("div[class=writing_view_box]").first();
                    Elements images = body.select("img");

                    //2. 각 글 순회
                    //찾는것 번호(number) .
                    // 말머리(header). 제목(title). 글쓴이(writer). 작성일(date).
                    // 조회(view) . 추천(recommend). 댓글수(NumberOfReply).
                    // 글종류(articleType). 글쓴이 고유 id(writerID)
                    int count=0;
                    for (Element image : images) {
                        String imgsrc = image.attr("onClick");
                        //System.out.println(imgsrc);
                        String res="p";
                        Pattern p = Pattern.compile("'([^']*)'");
                        Matcher m = p.matcher(imgsrc);
                        while (m.find()) {
                            String gg =m.group(1);
                            if(gg.contains("viewimagePop.php")){
                                res = gg;
                                break;
                            }
                        }
                        if(res.equals("p") )continue;
                        downloadImage(gallery.GetID()+""+coreID+""+count,res);
                        //UI업데이트
                        scene.updateValue(coreID, articles.size());
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    readd(coreID, pageURL);
                   // System.exit(1);
                }
            }
        }
    }
    private  void downloadImage(String strImageName, String strImageURL){

        System.out.println("Saving: "+ strImageURL);
        try {
            URL url = new URL(strImageURL);
            BufferedImage ifile = ImageIO.read(url);

            ImageIO.write(ifile,"png" , new File(gallery.GetID()+File.separator+strImageName));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void polishUp() {
        for (User aUserlist : Userlist) {
            aUserlist.calcActivity();
            int month, year;
            year = aUserlist.firstWrite.year;
            month = aUserlist.firstWrite.month - 1;
        //    months[year][month].newUserIDs.add(aUserlist.name);
        }

    }

    @Override
    public void writeCSV() {
        String mFilename = "excel_" + gallery.GetID() + "_month.csv";
        String uFIlename = "excel_" + gallery.GetID() + "_users.csv";
        String kFIlename = "excel_" + gallery.GetID() + "_keys.csv";
        PrintWriter mpw = getPrinter(mFilename);
        //Write Months
        mpw.write("Date,totalPost,totalReply,totalView,newUser,avgStay\n");
        mpw.close();
    }


}
