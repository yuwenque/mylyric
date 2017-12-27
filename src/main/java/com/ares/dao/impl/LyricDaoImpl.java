package com.ares.dao.impl;

import com.ares.dao.LyricDao;
import com.ares.entity.LyricEntity;
import com.gzkit.jaguar.mybatis.dao.BaseDao4Batis;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ares on 2017/12/25.
 */
@Repository("lyricDao")
public class LyricDaoImpl extends BaseDao4Batis<Long, LyricEntity> implements LyricDao {

    private static final String NAMESPACE = "com.ares.mapper.SongMapper";



    @Override
    public int insertLyric(String songId, String songName, String lyricContent) {

        Map<String,Object> map = new HashMap<>();
        map.put("songId",songId);
        map.put("songName",songName);
        map.put("lyricContent",lyricContent);
        return executeInsert("insertLyric",map);
    }

    @Override
    public LyricEntity findLyricBySongId(String songId) {
        Map<String,Object> map = new HashMap<>();
        map.put("songId",songId);
        return selectOne("findLyricBySongId",map);
    }

    @Override
    protected String getNamespace() {
        return NAMESPACE;
    }
}
