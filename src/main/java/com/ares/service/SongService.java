package com.ares.service;

import com.ares.entity.SongEntity;

import java.util.List;

/**
 * Created by ares on 2017/12/25.
 */
public interface SongService {


    void insertSong(String songId,String songName,String singerId);

    List<SongEntity> findSongList(String singerId);

}
