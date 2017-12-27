package com.ares.dao.impl;

import com.ares.dao.SingerDao;
import com.ares.entity.SingerEntity;
import com.gzkit.jaguar.mybatis.dao.BaseDao4Batis;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ares on 2017/12/27.
 */
@Repository("singerDao")
public class SingerDaoImpl extends BaseDao4Batis<Long, SingerEntity> implements SingerDao
{

    private static final String NAMESPACE = "com.ares.mapper.SongMapper";

    @Override
    public int insertSinger(SingerEntity singerEntity) {

      return   executeInsert("insertSinger",singerEntity);

    }

    @Override
    public SingerEntity getSingerById(String singerId) {
        Map<String,String > map = new HashMap<>();
        map.put("id",singerId);
        return selectOne("getSingerById",map);
    }

    @Override
    public SingerEntity getSingerByName(String singerName) {
        Map<String,String > map = new HashMap<>();
        map.put("name",singerName);
        List<SingerEntity> list = selectList("getSingerByName",singerName);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    protected String getNamespace() {
        return NAMESPACE;
    }
}
