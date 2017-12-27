package com.ares;

import java.util.List;

/**
 * Created by ares on 2017/12/22.
 */
public class KeywordToken {


    /**
     * code : 0
     * message :
     * codeDesc : Success
     * keywords : [{"keyword":"如果这都不算爱","score":0.61018645763397,"type":"keyword"}]
     */

    private int code;
    private String message;
    private String codeDesc;
    private List<KeywordsBean> keywords;

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

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public List<KeywordsBean> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeywordsBean> keywords) {
        this.keywords = keywords;
    }

    public static class KeywordsBean {
        /**
         * keyword : 如果这都不算爱
         * score : 0.61018645763397
         * type : keyword
         */

        private String keyword;
        private double score;
        private String type;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
