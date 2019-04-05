package com.ares.mapper;

import com.ares.entity.stock.Stock925;
import com.ares.entity.stock.StockAdd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StockMapper {

    int insertStock(StockAdd stockAdd);

    int insertStock925(Stock925 stock925);

    int deleteStockAdd(@Param("id") String id);

    List<StockAdd> queryAddStock(@Param("date") String date);

    Stock925 checkStock925(@Param("id") String id ,@Param("date") String date);



}
