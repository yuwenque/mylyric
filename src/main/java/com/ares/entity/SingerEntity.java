package com.ares.entity;

/**
 * Created by ares on 2017/12/27.
 */
public class SingerEntity {

    /**
     * id : 9615
     * name : 吴雨霏
     * picUrl : http://p1.music.126.net/sdow6GiZrSc59WuFbw6rVA==/164926744183505.jpg
     * alias : ["Kary Ng"]
     * albumSize : 33
     * picId : 164926744183505
     * img1v1Url : http://p1.music.126.net/Tqbi60X5qa8qZzgmB0lvTA==/267181325567047.jpg
     * img1v1 : 267181325567047
     * mvSize : 19
     * followed : false
     * alia : ["Kary Ng"]
     * trans : null
     */


    private String id;
    private String name;
    private String picUrl;
    private int albumSize;
    private long picId;
    private String img1v1Url;
    private long img1v1;
    private int mvSize;
    private boolean followed;
    private Object trans;

    public SingerEntity(){

    }
    public SingerEntity(String id, String name, String picUrl, int albumSize, long picId, String img1v1Url, long img1v1, int mvSize, boolean followed, Object trans) {
        this.id = id;
        this.name = name;
        this.picUrl = picUrl;
        this.albumSize = albumSize;
        this.picId = picId;
        this.img1v1Url = img1v1Url;
        this.img1v1 = img1v1;
        this.mvSize = mvSize;
        this.followed = followed;
        this.trans = trans;
    }

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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getAlbumSize() {
        return albumSize;
    }

    public void setAlbumSize(int albumSize) {
        this.albumSize = albumSize;
    }

    public long getPicId() {
        return picId;
    }

    public void setPicId(long picId) {
        this.picId = picId;
    }

    public String getImg1v1Url() {
        return img1v1Url;
    }

    public void setImg1v1Url(String img1v1Url) {
        this.img1v1Url = img1v1Url;
    }

    public long getImg1v1() {
        return img1v1;
    }

    public void setImg1v1(long img1v1) {
        this.img1v1 = img1v1;
    }

    public int getMvSize() {
        return mvSize;
    }

    public void setMvSize(int mvSize) {
        this.mvSize = mvSize;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public Object getTrans() {
        return trans;
    }

    public void setTrans(Object trans) {
        this.trans = trans;
    }
}
