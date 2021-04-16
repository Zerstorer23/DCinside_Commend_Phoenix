package lexington.GalleryAPI;

import static lexington.Main.TITLES_IN_PAGE;

public class Gallery {
    private String gallID;
    private boolean isMajor = true;

    public String GetID(){
        return  gallID;
    }

    public boolean IsMajor(){
        return isMajor;
    }

    public void setInfo(String gallID,boolean isMajor){
        this.gallID = gallID;
        this.isMajor = isMajor;
    }

    public String GetMainURL(){
        return (isMajor)?
                "http://gall.dcinside.com/board/lists/?id=" + gallID + "&list_num="+TITLES_IN_PAGE+"&sort_type=N&search_head=&page=":
                "http://gall.dcinside.com/mgallery/board/lists/?id=" + gallID + "&list_num="+TITLES_IN_PAGE+"&sort_type=N&search_head=&page=" ;
    }
}
