package com.ares.service.impl;

import com.ares.dao.SingerDao;
import com.ares.entity.SingerEntity;
import com.ares.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by ares on 2017/12/27.
 */
@Service("singerService")
public class SingerServiceImpl implements SingerService
{


    @Autowired
    @Qualifier("singerDao")
    SingerDao singerDao;

    @Override
    public int insertSinger(SingerEntity singerEntity) {
        return singerDao.insertSinger(singerEntity);
    }

    @Override
    public SingerEntity getSingerById(String singerId) {
        return singerDao.getSingerById(singerId);
    }

    @Override
    public SingerEntity getSingerByName(String singerName) {

        return singerDao.getSingerByName(singerName);
    }
}
