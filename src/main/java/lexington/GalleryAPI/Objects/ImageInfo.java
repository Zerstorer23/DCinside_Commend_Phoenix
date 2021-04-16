package lexington.GalleryAPI.Objects;

public class ImageInfo {

    String postURL,srcURL;

    public String getPostURL(){
        return postURL;
    }
    public String getsrcURL(){
        return srcURL;
    }
    public void setURL(String post, String imgsrc){
        this.postURL=post;
        this.srcURL = imgsrc;
    }

}
