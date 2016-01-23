package com.itw.georesearch.model;

public class Ikw {

    private Long ID;

    private String LOCATION;

    private String INDICATOR;

    private String SUBJECT;

    private String MEASURE;

    private String FREQUENCY;

    private String TIME;

    private Double Value;

    private String Flag_Codes;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getINDICATOR() {
        return INDICATOR;
    }

    public void setINDICATOR(String INDICATOR) {
        this.INDICATOR = INDICATOR;
    }

    public String getSUBJECT() {
        return SUBJECT;
    }

    public void setSUBJECT(String SUBJECT) {
        this.SUBJECT = SUBJECT;
    }

    public String getMEASURE() {
        return MEASURE;
    }

    public void setMEASURE(String MEASURE) {
        this.MEASURE = MEASURE;
    }

    public String getFREQUENCY() {
        return FREQUENCY;
    }

    public void setFREQUENCY(String FREQUENCY) {
        this.FREQUENCY = FREQUENCY;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public Double getValue() {
        return Value;
    }

    public void setValue(Double value) {
        Value = value;
    }

    public String getFlag_Codes() {
        return Flag_Codes;
    }

    public void setFlag_Codes(String flag_Codes) {
        Flag_Codes = flag_Codes;
    }
}

