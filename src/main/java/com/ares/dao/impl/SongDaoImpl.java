package com.ares.dao.impl;

import com.ares.dao.SongDao;
import com.ares.entity.SongEntity;
import com.gzkit.jaguar.mybatis.dao.BaseDao4Batis;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ares on 2017/12/25.
 */

@Repository("songDao")
public class SongDaoImpl extends BaseDao4Batis<Long, SongEntity> implements SongDao {


    private static final String NAMESPACE = "com.ares.mapper.SongMapper";


    @Override
    public int insertSong(String songId, String songName, String singerId) {

        Map<String,Object> map = new HashMap<>();
        map.put("songId",songId);
        map.put("songName",songName);
        map.put("singerId",singerId);

        return executeInsert("insertSong",map);
    }

    @Override
    public List<SongEntity> getSongList(String singerId) {


        Map<String,Object> map = new HashMap<>();
        map.put("singerId",singerId);
        return selectList("getSongListBySingerId",map);
    }

    @Override
    protected String getNamespace() {
        return NAMESPACE;
    }
}
