package lexington.GalleryAPI.Objects;

public class Article {
    String title,header;
    public int id;
    String writer,writerID,ipAddress;
    String date;
    public int view,recommend,numberOfReply;
    ArticleType type = ArticleType.ETC;
    boolean isBest =false;
    boolean isIP = false;

    String URL;

    //말머리(header). 제목(title). 글쓴이(writer). 작성일(date).
    // 조회(view) . 추천(recommend). 댓글수(NumberOfReply).
    // 글종류(articleType). 글쓴이 고유 id(writerID)
    public void setType(String typeInfo){
        if(typeInfo.equals("icon_img icon_txt")){ //
            type=ArticleType.TEXT;
        }else if(typeInfo.equals("icon_img icon_pic")){
            type=ArticleType.PICTURE;
        }else if(typeInfo.equals("icon_img icon_recomimg")){
            isBest = true;
            type=ArticleType.PICTURE;
        }else if(typeInfo.equals("icon_img icon_recomtxt")){
            isBest = true;
            type=ArticleType.TEXT;
        }else{
            //System.out.println("아이콘 맞지않음"+this.title+" "+this.date);
        }
    }
    public ArticleType getType(){
        return type;
    }
    public String getURL(){
        return URL;
    }
    public void setTitleInfo(int id, String header, String title){
        this.id = id;
        this.header=header;
        this.title = title;

    }
    public void setNumericInfo(int view, int reco, int replies){
        this.view=view;
        this.recommend = reco;
        this.numberOfReply = replies;
    }
    public void setWriterInfo(boolean isyudong, String writer, String ID){
        this.writer = writer;
        if(isyudong){
            this.ipAddress = ID;
        }else{
            this.writerID = ID;
        }
        isIP = isyudong;

    }
    public void setDate(String date){
        this.date = date;
    }

    public void setURL(String lll){
        this.URL = lll;
    }
    public String toString(){
        return "["+id+"] "+header+" / "+title+"\n" +
                writer+" ("+writerID+"/"+ipAddress+") \n"+
                date+" / 조회 "+view+"/ 댓글 "+numberOfReply+" / 추천 "+recommend+" \n"+
                "종류: "+type+" 개념: "+isBest+" 유동: "+isIP+"\n"+
                this.URL;

    }
}
