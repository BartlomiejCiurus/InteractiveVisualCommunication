package com.itw.georesearch.support.enums;

public enum Indicators {

    EMP("Employment rate"),
    EMPAGE("Employment rate by age group"),
    EMPEDU("Employment by education"),
    EMPINDUS("Employment by activity"),
    PARTEMP("Part-time employment"),
    SELFEMP("Self-employment rate"),
    TEMPEMP("Temporary employment"),
    LF("Labour force"),
    LFFORECAST("Labour force forecast"),
    LFPR("Labour force participation rate"),
    HRWKD("Hours worked");

    private String indicator;

    Indicators(String indicator) {
        this.indicator = indicator;
    }

    @Override
    public String toString() {
        return indicator;
    }

    public static String getEnumByString(String code){
        for(Indicators e : Indicators.values()){
            if(code.equals(e.indicator)) return e.name();
        }
        return null;
    }
}
