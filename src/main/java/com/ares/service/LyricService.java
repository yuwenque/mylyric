package com.ares.service;

import com.ares.entity.LyricEntity;

/**
 * Created by ares on 2017/12/25.
 */
public interface LyricService {

    void insertLyric(String songId,String songName,String lyricContent);
    LyricEntity findLyricBySongId(String songId);

}
