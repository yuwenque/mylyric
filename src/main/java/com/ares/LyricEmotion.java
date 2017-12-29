package com.ares;

import com.ares.http.TextSentimentResult;

/**
 * Created by ares on 2017/12/21.
 */
public class LyricEmotion {


    private String songId;
    private String songName;
    private TextSentimentResult emotion;

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

    public TextSentimentResult getEmotion() {
        return emotion;
    }

    public void setEmotion(TextSentimentResult emotion) {
        this.emotion = emotion;
    }

    @Override
    public String toString() {
        return "LyricEmotion{" +
                "songName='" + songName + '\'' +
                ", emotion=" + emotion +
                '}';
    }
}
