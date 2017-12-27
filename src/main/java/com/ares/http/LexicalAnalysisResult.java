package com.ares.http;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ares on 2017/12/22.
 */
public class LexicalAnalysisResult {


    /**
     * code : 0
     * message :
     * combtokens : [{"cls":"短语","pos":0,"wlen":"8","word":"我爱洗澡"}]
     * tokens : [{"pos":0,"wlen":"2","word":"我","wtype":"代词","wtype_pos":27},{"pos":2,"wlen":"2","word":"爱","wtype":"动词","wtype_pos":31},{"pos":4,"wlen":"4","word":"洗澡","wtype":"动词","wtype_pos":31}]
     */

    private int code;
    private String message;
    private List<CombtokensBean> combtokens = new ArrayList<>();
    private List<TokensBean> tokens= new ArrayList<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CombtokensBean> getCombtokens() {
        return combtokens;
    }

    public void setCombtokens(List<CombtokensBean> combtokens) {
        this.combtokens = combtokens;
    }

    public List<TokensBean> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokensBean> tokens) {
        this.tokens = tokens;
    }

    public static class CombtokensBean {
        /**
         * cls : 短语
         * pos : 0
         * wlen : 8
         * word : 我爱洗澡
         */

        private String cls;
        private int pos;
        private String wlen;
        private String word;

        public String getCls() {
            return cls;
        }

        public void setCls(String cls) {
            this.cls = cls;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public String getWlen() {
            return wlen;
        }

        public void setWlen(String wlen) {
            this.wlen = wlen;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }

    public static class TokensBean {
        /**
         * pos : 0
         * wlen : 2
         * word : 我
         * wtype : 代词
         * wtype_pos : 27
         */

        private int pos;
        private String wlen;
        private String word;
        private String wtype;
        private int wtype_pos;

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public String getWlen() {
            return wlen;
        }

        public void setWlen(String wlen) {
            this.wlen = wlen;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getWtype() {
            return wtype;
        }

        public void setWtype(String wtype) {
            this.wtype = wtype;
        }

        public int getWtype_pos() {
            return wtype_pos;
        }

        public void setWtype_pos(int wtype_pos) {
            this.wtype_pos = wtype_pos;
        }
    }
}
