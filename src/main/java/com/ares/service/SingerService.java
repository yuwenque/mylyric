package com.ares.service;

import com.ares.entity.SingerEntity;

/**
 * Created by ares on 2017/12/27.
 */
public interface SingerService {

    int insertSinger(SingerEntity singerEntity);

    SingerEntity getSingerById(String singerId);

    SingerEntity getSingerByName(String singerName);

}
