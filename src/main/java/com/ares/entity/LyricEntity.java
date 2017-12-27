package com.ares.entity;

/**
 * Created by ares on 2017/12/25.
 */
public class LyricEntity {

    private String songName;
    private String songId;
    private String lyricContent;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getLyricContent() {
        return lyricContent;
    }

    public void setLyricContent(String lyricContent) {
        this.lyricContent = lyricContent;
    }
}
