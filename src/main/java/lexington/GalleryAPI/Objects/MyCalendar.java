package lexington.GalleryAPI.Objects;



public class MyCalendar {
   public int year;
    public int month;
    int day;
    public MyCalendar(int y, int m, int d){
        year = y;
        month = m;
        day = d;
    }
    public String toString(String galleryYear){
        return (year+galleryYear+2000)+"-"+(month)+"-"+day;
    }
    public String toStringinDays(){
        int days = year * 365 + month * 31 + day;
        return days+"";
    }
    String readableString(){
        String content = " ";
        if(year>0){
            content = content + year+"년 ";
        }if(month>0){
            content = content + month+"개월 ";
        }if(day>0){
            content = content + day+"일 ";
        }else{
            content = content + " 1일 ";
        }

        return content;
    }
}
