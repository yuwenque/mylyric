package com.ares.entity.stock;

public class StockAdd {


    private String id;
    private String name;
    private long vol_on_up;
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVol_on_up() {
        return vol_on_up;
    }

    public void setVol_on_up(long vol_on_up) {
        this.vol_on_up = vol_on_up;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
