package com.ares.service.impl;

import com.ares.dao.SongDao;
import com.ares.entity.SongEntity;
import com.ares.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ares on 2017/12/25.
 */
@Service("songService")
public class SongServiceImpl implements SongService {


    @Autowired
    @Qualifier(value = "songDao")
    SongDao songDao;

    @Override
    public void insertSong(String songId, String songName, String singerId) {


        songDao.insertSong(songId,songName,singerId);
    }

    @Override
    public List<SongEntity> findSongList(String singerId) {
        return songDao.getSongList(singerId);
    }
}
