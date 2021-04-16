package lexington.GalleryAPI;


import lexington.GalleryAPI.GUI.mainScene;
import lexington.GalleryAPI.Objects.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

import static lexington.GalleryAPI.Preprocessor.getPrinter;


public class Crawler_title extends Crawler {


    /**
     * 갤러리의 모든 글 제목을 긁어내는 코드입니다.
     * 유지보수 쟁점
     */
    public Crawler_title(mainScene scene, Gallery gall, int numberOfMultiThreads, int MaxPageNumber, int StepSize){
        this.gallery = gall;
        this.cores = numberOfMultiThreads;
        this.maxPageToScroll = MaxPageNumber;
        this.stepSize=StepSize;
        this.scene = scene;
    }

    public void scrollRaw() {
        initCores();
        ArrayList<DownloadCore> activeCores = new ArrayList<>();
        for (int c = 0; c < cores; c++) {
            DownloadCore go = new DownloadCore(c);
            activeCores.add(go);
            go.start();
        }

        for (int c = 0; c < cores; c++) {
            try {
                activeCores.get(c).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(c+ "has finished...");
        }
        System.out.println("Scroll done!");
        scene.dispose();
    }

    public void scrollOnePage(){
        Queue<String> lists = new LinkedList<>();
        CORES.add(lists);
        CORES.get(0).add(gallery.GetMainURL()+"1");


        core_sizes = new int[1];
        core_sizes[0] = 1;


        DownloadCore go = new DownloadCore(0);
        go.start();
            try {
                go.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
    public class DownloadCore extends Thread {
        int coreID;
        DownloadCore(int c){
            coreID = c;
        }

        public void run(){
            while (!CORES.get(coreID).isEmpty()) {
                String pageURL = CORES.get(coreID).poll();
                try {

                    //1. 게시판 body 추출
                    Document doc = Jsoup.connect(pageURL).get();
                    Elements posts = doc.select("tbody").first().select("tr[class=ub-content],tr[class=ub-content us-post]");
                    assert posts != null;

                    //2. 각 글 순회
                    //찾는것 번호(number) .
                    // 말머리(header). 제목(title). 글쓴이(writer). 작성일(date).
                    // 조회(view) . 추천(recommend). 댓글수(NumberOfReply).
                    // 글종류(articleType). 글쓴이 고유 id(writerID)
                    for (Element post : posts) {
                        String header = "일반";
                        String title,writer,date,typeInfo,writerID;
                        int number,view,recommend,NumberOfReply;
                        boolean isYudong=true;
                     //   System.out.println();
                      //  System.out.println(post.html());

                        String numText= post.select("td[class=gall_num]").text();
                        if(!isNumeric(numText)) continue;
                        //메이져갤은 id가 공지일수있다. 스킵
                        number = Integer.parseInt(numText);

                        if(!gallery.IsMajor()) {
                            header = post.select("td[class=gall_subject]").first().text();
                            if (header.equals("공지")) continue;
                            //공지 싸그리 무시->dksgka. 마이너갤 공지는 1페이지만 나옴
                        }
                        Element ahref=post.select("td[class=gall_tit ub-word],td[class=gall_tit ub-word voice_tit]").first().select("a").first();
                        title = ahref.text();
                        String URL = ahref.attr("href");
                        Element articleDoc = post.select("em[class").first();
                        typeInfo=articleDoc.attr("class");


                        Element writerDoc =post.select("td[class=gall_writer ub-writer]").first();
                            writer = cleanseNick(writerDoc.attr("data-nick"));
                            writerID = writerDoc.attr("data-ip");
                            if(writerID.length() ==0) {
                                isYudong = false;
                                writerID =writerDoc.attr("data-uid");
                            }

                        view = Integer.parseInt(post.select("td[class=gall_count]").text());
                        recommend = Integer.parseInt(post.select("td[class=gall_recommend]").text());
                        date = post.select("td[class=gall_recommend]").attr("title");
                        //Replies
                        Element ReplyBox = post.select("span[class=reply_num]").first();
                        String replies = "0";
                        if (ReplyBox != null) {
                            replies = ReplyBox.text();
                            replies = removeChars(replies).split(" ")[0];
                        }
                        NumberOfReply = Integer.parseInt(replies);

                        //3. 데이터 정리
                        Article article = new Article();
                        article.setTitleInfo(number,header,title);
                        article.setDate(date);
                        article.setNumericInfo(view,recommend,NumberOfReply);
                        article.setType(typeInfo);
                        article.setWriterInfo(isYudong,writer,writerID);
                        article.setURL(URL);

                        // 추가
                        articles.add(article);

                        //UI업데이트
                        scene.updateValue(coreID, articles.size());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    readd(coreID, pageURL);
                  //  System.exit(1);
                }
            }
        }
        private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        public boolean isNumeric(String strNum) {
            if (strNum == null) {
                return false;
            }
            return pattern.matcher(strNum).matches();
        }
    }


    @Override
    public void polishUp() {


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
