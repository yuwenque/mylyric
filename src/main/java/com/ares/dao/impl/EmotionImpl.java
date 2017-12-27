package com.ares.dao.impl;

import com.ares.dao.EmotionDao;
import com.ares.entity.SongEmotionEntity;
import com.gzkit.jaguar.mybatis.dao.BaseDao4Batis;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ares on 2017/12/25.
 */
@Repository("emotionDao")
public class EmotionImpl extends BaseDao4Batis<Long, SongEmotionEntity> implements EmotionDao {

    private static final String NAMESPACE = "com.ares.mapper.SongMapper";


    @Override
    public int insertSongEmotion(String songId, String songName, double positive, double negative) {

        Map<String,Object> map = new HashMap<>();
        map.put("songId",songId);
        map.put("songName",songName);
        map.put("positive",positive);
        map.put("negative",negative);
        return executeInsert("insertSongEmotion",map);
    }

    @Override
    public SongEmotionEntity findSongEmotionBySongId(String songId) {
        Map<String,Object> map = new HashMap<>();
        map.put("songId",songId);

        return selectOne("findSongEmotionBySongId",map);
    }

    @Override
    protected String getNamespace() {
        return NAMESPACE;
    }
}
