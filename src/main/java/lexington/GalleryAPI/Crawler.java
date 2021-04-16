package lexington.GalleryAPI;

import lexington.GalleryAPI.GUI.mainScene;
import lexington.GalleryAPI.Objects.Article;

import java.util.*;


/**
 * 갤러리 정보 긁는 크롤러
 * abstract 클래스로 만들어서 새로운 형태의 크롤러 제작시
 * 간단하게 복사 교체가능
 */
public abstract class Crawler {

    /*
    * 멀티스레드로
    * 여러개의 스레드가 각각 특정 페이지를 담당하여 긁는 원리
    * */

    //스레드별로 담당하는 갤러리 페이지 url을 큐로 저장

    //시스템정보
    static ArrayList<Queue<String>> CORES = new ArrayList<>();
    protected int[] core_sizes;

    private HashMap<String, Integer> attempts = new HashMap<>();
    protected int cores = 10;
    protected int maxPageToScroll =0;
    protected int stepSize = 1;

    //갤러리정보
    Gallery gallery;
    protected ArrayList<Article> articles = new ArrayList<>();
    protected mainScene scene;

    void initCores() {
        for (int i = 0; i < cores; i++) {
            Queue<String> lists = new LinkedList<>();
            CORES.add(lists);
        }
        String tempPage;
        for (int p = 1; p <= maxPageToScroll; p = p + stepSize) {
            tempPage = gallery.GetMainURL() + p;
            //  System.out.println(tempPage);
            int index = p % cores;
            CORES.get(index).add(tempPage);
        }

        core_sizes = new int[cores];
        for (int i = 0; i < cores; i++) {
            core_sizes[i] = CORES.get(i).size();
            scene.addLabel(i,core_sizes[i]);
        }
    }

    void readd(int coreID, String page) {
        System.out.println("Status exception: Re-add");
        if (attempts.containsKey(page)) {
            int tried = attempts.get(page);
            System.out.println(page + "  Attempts:" + tried);
            if (tried < 12) {
                CORES.get(coreID).add(page);
                attempts.replace(page, tried + 1);
            } else {
                System.out.println("Stop readding");
            }
        } else {
            CORES.get(coreID).add(page);
            attempts.put(page, 1);
            System.out.println("[NEW]" + page + "  Attempts:" + 1);
        }
        System.out.println("  Attempt SIZE=" + attempts.size());
    }

    public abstract void scrollRaw();
    public abstract void scrollOnePage();
    public abstract void polishUp();

    public abstract void writeCSV();

    private static String regexNumber ="\\d+";
    static boolean isValidIndex(String aa){
        return aa.matches(regexNumber);
    }

    public void print(){
        for(Article a : articles){ System.out.println(a.toString());
            System.out.println();

        }
    }
    public int getSize(){
        return articles.size();
    }
    public ArrayList<Article> getArticles(){
        return this.articles;
    }

    static String removeChars(String title) {
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        title = title.replaceAll(match, " ");
        title = title.replace("dc", "");
        title = title.replace("official", "");
        title = title.replace("app", "");
        title = title.replace("앱에서", "");
        title = title.replace("작성", "");
        title = title.replace("http", "");
        title = title.replace("/", "");
        title = title.replace("\n", "");
        title = title.trim().replaceAll(" +", " ");
        return title.toLowerCase();
    }

    static String cleanseNick(String nick) {
        nick = nick.replace(",", ".");
        nick = nick.replace("/", ".");
        nick = nick.replace("\n", " ");
        nick = nick.trim().replaceAll(" +", " ");
        return nick;
    }

    public boolean isReadyToBuild() {
        boolean ready = true;
        System.out.println(" ");
        for (int i = 0; i < cores; i++) {
            double total = core_sizes[i];
            int completed = (int) total - CORES.get(i).size();
            System.out.println("[CORE #" + i + "] Completed: " + completed + " | " + (int) ((completed / total) * 100) + "%");
            if (!CORES.get(i).isEmpty()) ready = false;
        }
        if (ready) {
            for (Map.Entry<String, Integer> attempt : attempts.entrySet()) {
                    int tried = attempt.getValue();
                    System.out.println(attempt.getKey()+" : "+attempt.getValue());
                    if (tried < 1) {
                        ready = false;
                        break;
                    }
            }
        }
        System.out.println(" ");
        return ready;
    }
    void lock(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
            //need to wait
            while (!isReadyToBuild()) {
                System.out.println("[POLL] Not Ready...");
                Thread.sleep(seconds * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
