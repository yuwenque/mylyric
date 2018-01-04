package com.ares.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ares on 2018/1/4.
 */
public class ArtistDetail {




    private List<String> titleList=new ArrayList<>();
    private List<String> contentList=new ArrayList<>();
    private String headerTitle;


    public List<String> getTitleList() {
        return titleList;
    }
    public void addTitle(String title) {
        this.titleList.add(title);

    }
    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }

    public List<String> getContentList() {
        return contentList;
    }

    public void setContentList(List<String> contentList) {
        this.contentList = contentList;
    }
    public void addContent(String content) {
        this.contentList.add(content);

    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    @Override
    public String toString() {
        return "ArtistDetail{" +
                "titleList=" + titleList +
                ", contentList=" + contentList +
                ", headerTitle='" + headerTitle + '\'' +
                '}';
    }
}
