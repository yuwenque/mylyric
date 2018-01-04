package com.ares.entity;

/**
 * Created by ares on 2018/1/4.
 */
public class TitleContentBean {

    private String content;
    private int type=TITLE;

    public TitleContentBean() {
    }

    public TitleContentBean(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content.replace("{}","\n\n");
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static final int TITLE=0;
    public static final int CONTENT=1;
}
