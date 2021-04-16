package lexington.GalleryAPI.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import static lexington.GalleryAPI.Objects.Hash_storage.foundUsers;

public class Month {
    private MyCalendar myCalendar;
    public int totalView;
    public int totalPost;
    public int totalReply;
    public int avgStay;
    public ArrayList<String> newUserIDs = new ArrayList<>();
    public HashMap<String, Integer> vocab_freq = new HashMap<>();

    public Month() {

    }

    public Month(int y, int m) {
        myCalendar = new MyCalendar(y, m, 0);
    }

    public void addSentence(String content) {
        //      sentences.add(content);
        String[] token = content.split(" ");
        for (int i = 0; i < token.length; i++) {
            if (token[i].length() <= 1) continue;
            if (Pattern.matches("[a-zA-Z]+", token[i])) continue;
            if (vocab_freq.containsKey(token[i])) {
                vocab_freq.replace(token[i], vocab_freq.get(token[i]) + 1);
            } else {
                vocab_freq.put(token[i], 1);
            }

        }
    }

    public void appendView(int a) {
        this.totalView = this.totalView + a;
    }

    public void incrementPost() {
        this.totalPost++;

    }

    public void appendReply(int a) {
        this.totalReply = this.totalReply + a;
    }

    public void appendUser(String ID) {
        if (!foundUsers.contains(ID)) {
            foundUsers.add(ID);
            //  newUserIDs.add(ID);
        }
    }


}
