package com.ares.service;

import com.ares.entity.SongEmotionEntity;

/**
 * Created by ares on 2017/12/25.
 */
public interface SongEmotionService {


    void insertSongEmotion(String songId, String songName, double positive, double negative);
    SongEmotionEntity findSongEmotionBySongId(String songId);


}
