package com.itw.georesearch.support.enums;

public enum Frequency {

    A("Yearly"),
    Q("Quarterly"),
    M("Monthly");

    private String frequency;

    Frequency(String frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return frequency;
    }

    public static String getEnumByString(String code){
        for(Frequency e : Frequency.values()){
            if(code.equals(e.frequency)) return e.name();
        }
        return null;
    }

}
