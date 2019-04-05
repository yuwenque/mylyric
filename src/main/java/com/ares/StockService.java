package com.ares;

import com.ares.entity.stock.Stock925;
import com.ares.entity.stock.StockAdd;

import java.util.List;

public interface StockService {

    int insertStock(StockAdd stockAdd);

    int insertStock925(Stock925 stock925);

    int deleteStockAdd(String id);

    List<StockAdd> queryAddStock(String date);

    Stock925 checkStock925(String id ,String date);
}
