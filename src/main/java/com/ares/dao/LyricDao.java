package com.ares.dao;

import com.ares.entity.LyricEntity;
import com.gzkit.jaguar.mybatis.dao.IBaseDao4Batis;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by ares on 2017/12/25.
 */
@Mapper
public interface LyricDao extends IBaseDao4Batis<Long, LyricEntity> {





    int insertLyric(String songId, String songName, String lyricContent);

    LyricEntity findLyricBySongId(String songId);

}
