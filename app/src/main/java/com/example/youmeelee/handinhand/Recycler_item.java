package com.example.youmeelee.handinhand;

/**
 * Created by youmeelee on 16. 5. 28..
 */
public class Recycler_item  {
    private  String image_path;
    private String video_date;
    private String user_a;
    private String user_b;
    private String temp_a;
    private String temp_b;
    private String timeline_id;
    private String all;

    public Recycler_item(){}

    public String getAll(){
        all = timeline_id + "\n" + user_a + "\n" + temp_a + "\n" + user_b + "\n" + temp_b + "\n" + video_date + "\n";

        return all;
    }
    public void printTimeline() {
        System.out.println(timeline_id + " " + user_a + " " + temp_a + " " + user_b + " " + temp_b + " " + video_date + " " + image_path);
    }
    public Recycler_item( String image_path, String video_date,String user_a, String user_b
            , String temp_a, String temp_b, String timeline_id){
        this.image_path = image_path;
        this.temp_a = temp_a;
        this.temp_b  = temp_b;
        this.user_a = user_a;
        this.user_b = user_b;
        this.timeline_id = timeline_id;
        this.video_date = video_date;
    }
    public String getTimelineId(){return  timeline_id;}
    public  String getImage(){
        return image_path;
    }
    public String getVideo_date(){
        return video_date;
    }

    public String getUser_a(){ return user_a;}
    public String getUser_b(){
        return user_b;
    }
    public String getTemp_a(){
        return temp_a;
    }
    public String getTemp_b(){
        return temp_b;
    }

    public void setTimeline_id(String timeline_id){
        this.timeline_id = timeline_id;
    }
    public void setImage(String image_path){
        this.image_path = image_path;
    }
    public void setVideo_date(String video_date){
        this.video_date = video_date;
    }
    public void setUser_a(String user_a){
        this.user_a = user_a;
    }
    public void setUser_b(String user_b){
        this.user_b = user_b;
    }
    public void setTemp_a(String temp_a){
        this.temp_a = temp_a;
    }
    public void setTemp_b(String temp_b){ this.temp_b = temp_b;}



}