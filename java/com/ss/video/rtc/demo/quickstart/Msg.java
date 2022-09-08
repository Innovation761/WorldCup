package com.ss.video.rtc.demo.quickstart;

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    private String content;
    private int type;
    private String user;

    public Msg(String content, int type, String user){
        this.content = content;
        this.type = type;
        this.user = user;
    }

    public String getContent(){
        return content;
    }

    public String getUser() {return user;}

    public int getType(){
        return type;
    }

}
