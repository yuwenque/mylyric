package com.ares;

import com.ares.entity.stock.Stock925;
import com.ares.entity.stock.StockAdd;
import com.ares.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StockServiceImpl implements StockService{


    @Autowired
    StockMapper stockMapper;


    @Override
  public   int insertStock(StockAdd stockAdd){

      return stockMapper.insertStock(stockAdd);
  }

    @Override
    public   int insertStock925(Stock925 stock925){

      return stockMapper.insertStock925(stock925);
    }

    @Override
    public   int deleteStockAdd(String id){

      return stockMapper.deleteStockAdd(id);
    }

    @Override
    public   List<StockAdd> queryAddStock(String date){

      return stockMapper.queryAddStock(date);
    }

    @Override
    public   Stock925 checkStock925(String id ,String date){

      return stockMapper.checkStock925(id,date);
    }



}
