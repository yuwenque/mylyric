package com.ares.service.impl;

import com.ares.dao.LyricDao;
import com.ares.entity.LyricEntity;
import com.ares.service.LyricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by ares on 2017/12/25.
 */
@Service("lyricService")
public class LyricServiceImpl implements LyricService {


    @Autowired
    @Qualifier(value = "lyricDao")
    LyricDao lyricDao;

    @Override
    public void insertLyric(String songId, String songName, String lyricContent) {

        lyricDao.insertLyric(songId,songName,lyricContent);
    }

    @Override
    public LyricEntity findLyricBySongId(String songId) {
        return lyricDao.findLyricBySongId(songId);
    }
}
