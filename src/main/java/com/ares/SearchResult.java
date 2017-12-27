package com.ares;

import java.util.List;

/**
 * Created by ares on 2017/12/21.
 */
public class SearchResult {


    /**
     * result : {"artistCount":1,"queryCorrected":["吴雨霏"],"artists":[{"id":9615,"name":"吴雨霏","picUrl":"http://p1.music.126.net/sdow6GiZrSc59WuFbw6rVA==/164926744183505.jpg","alias":["Kary Ng"],"albumSize":33,"picId":164926744183505,"img1v1Url":"http://p1.music.126.net/Tqbi60X5qa8qZzgmB0lvTA==/267181325567047.jpg","img1v1":267181325567047,"mvSize":19,"followed":false,"alia":["Kary Ng"],"trans":null}]}
     * code : 200
     */

    private ResultBean result;
    private int code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ResultBean {
        /**
         * artistCount : 1
         * queryCorrected : ["吴雨霏"]
         * artists : [{"id":9615,"name":"吴雨霏","picUrl":"http://p1.music.126.net/sdow6GiZrSc59WuFbw6rVA==/164926744183505.jpg","alias":["Kary Ng"],"albumSize":33,"picId":164926744183505,"img1v1Url":"http://p1.music.126.net/Tqbi60X5qa8qZzgmB0lvTA==/267181325567047.jpg","img1v1":267181325567047,"mvSize":19,"followed":false,"alia":["Kary Ng"],"trans":null}]
         */

        private int artistCount;
        private List<String> queryCorrected;
        private List<ArtistsBean> artists;

        public int getArtistCount() {
            return artistCount;
        }

        public void setArtistCount(int artistCount) {
            this.artistCount = artistCount;
        }

        public List<String> getQueryCorrected() {
            return queryCorrected;
        }

        public void setQueryCorrected(List<String> queryCorrected) {
            this.queryCorrected = queryCorrected;
        }

        public List<ArtistsBean> getArtists() {
            return artists;
        }

        public void setArtists(List<ArtistsBean> artists) {
            this.artists = artists;
        }

        public static class ArtistsBean {
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

            private int id;
            private String name;
            private String picUrl;
            private int albumSize;
            private long picId;
            private String img1v1Url;
            private long img1v1;
            private int mvSize;
            private boolean followed;
            private Object trans;
            private List<String> alias;
            private List<String> alia;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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

            public List<String> getAlias() {
                return alias;
            }

            public void setAlias(List<String> alias) {
                this.alias = alias;
            }

            public List<String> getAlia() {
                return alia;
            }

            public void setAlia(List<String> alia) {
                this.alia = alia;
            }
        }
    }
}
