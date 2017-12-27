package com.ares.dao;

import com.ares.entity.SingerEntity;

/**
 * Created by ares on 2017/12/27.
 */
public interface SingerDao {

    int insertSinger(SingerEntity singerEntity);

    SingerEntity getSingerById(String singerId);

    SingerEntity getSingerByName(String singerName);

}
