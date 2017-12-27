package com.ares.service.impl;

import com.ares.dao.EmotionDao;
import com.ares.entity.SongEmotionEntity;
import com.ares.service.SongEmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by ares on 2017/12/25.
 */
@Service("songEmotionService")
public class SongEmotionServiceImpl implements SongEmotionService {
    @Autowired
    @Qualifier(value = "emotionDao")
    EmotionDao emotionDao;


    @Override
    public void insertSongEmotion(String songId, String songName, double positive, double negative) {

        emotionDao.insertSongEmotion(songId,songName,positive,negative);
    }

    @Override
    public SongEmotionEntity findSongEmotionBySongId(String songId) {
        return emotionDao.findSongEmotionBySongId(songId);
    }
}
