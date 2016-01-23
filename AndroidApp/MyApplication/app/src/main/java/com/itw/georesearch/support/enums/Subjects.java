package com.itw.georesearch.support.enums;

public enum Subjects {

    TOT("Total"),
    MEN("Men"),
    WOMEN("Women"),
    BUPPSRY("Below upper secondary"),
    TRY("Tertiary"),
    UPPSRY_NTRY("Upper secondary"),
    AGR("Agriculture"),
    CONSTR("Construction"),
    INDUSCONSTR("Industry including construction"),
    MFG("Manufacturing"),
    SERV("Services");

    private String subject;

    Subjects(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return subject;
    }

    public static String getEnumByString(String code){
        for(Subjects e : Subjects.values()){
            if(code.equals(e.subject)) return e.name();
        }
        return null;
    }

}
