package com.ares.entity.stock;

import java.util.List;

public class StockListBody {

    private String createTime;
    private List<StockAdd> list;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<StockAdd> getList() {
        return list;
    }

    public void setList(List<StockAdd> list) {
        this.list = list;
    }
}
