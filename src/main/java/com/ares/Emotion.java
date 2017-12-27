package com.ares;

/**
 * Created by ares on 2017/12/21.
 */
public class Emotion {


    /**
     * code : 0
     * message :
     * codeDesc : Success
     * positive : 0.5565470457077
     * negative : 0.44345292448997
     */

    private int code;
    private String message;
    private String codeDesc;
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

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
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
}
