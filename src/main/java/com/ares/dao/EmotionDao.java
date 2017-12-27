package com.ares.dao;

import com.ares.entity.SongEmotionEntity;
import com.gzkit.jaguar.mybatis.dao.IBaseDao4Batis;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by ares on 2017/12/25.
 */

@Mapper
public interface EmotionDao extends IBaseDao4Batis<Long, SongEmotionEntity> {


    int insertSongEmotion(String songId, String songName, double positive, double negative);

    SongEmotionEntity findSongEmotionBySongId(String songId);

}
