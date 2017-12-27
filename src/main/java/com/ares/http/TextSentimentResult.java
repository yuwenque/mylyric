package com.ares.http;

/**
 * Created by ares on 2017/12/20.
 */
public class TextSentimentResult {

    private int code ;
    private String message;
    private double positive;
    private double negative;

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

    public double getPositive() {
        return positive;
    }

    public void setPositive(double positive) {
        this.positive = positive;
    }

    public double getNegative() {
        return negative;
    }

    public void setNegative(double negative) {
        this.negative = negative;
    }

    @Override
    public String toString() {
        return "TextSentimentResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", positive=" + positive +
                ", negative=" + negative +
                '}';
    }
}
