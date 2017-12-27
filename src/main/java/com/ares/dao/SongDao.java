package com.ares.dao;

import com.ares.entity.SongEntity;
import com.gzkit.jaguar.mybatis.dao.IBaseDao4Batis;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by ares on 2017/12/25.
 */

@Mapper
public interface SongDao extends IBaseDao4Batis<Long, SongEntity> {





    int insertSong(String songId,String songName,String singerId);


    List<SongEntity>  getSongList(String singerId);
}
