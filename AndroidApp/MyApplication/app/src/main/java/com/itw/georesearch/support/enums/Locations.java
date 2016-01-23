package com.itw.georesearch.support.enums;

public enum Locations {
    AUS(""),
    AUT(""),
    BEL(""),
    CAN(""),
    CZE(""),
    DNK(""),
    FIN(""),
    FRA(""),
    DEU(""),
    GRC(""),
    HUN(""),
    ISL(""),
    IRL(""),
    ITA(""),
    JPN(""),
    KOR(""),
    LUX(""),
    MEX(""),
    NLD(""),
    NZL(""),
    NOR(""),
    POL(""),
    PRT(""),
    SVK(""),
    ESP(""),
    SWE(""),
    CHE(""),
    TUR(""),
    GBR(""),
    USA(""),
    BRA(""),
    CHL(""),
    EST(""),
    IDN(""),
    ISR(""),
    RUS(""),
    SVN(""),
    ZAF(""),
    EU28(""),
    OECD(""),
    G7(""),
    EA19(""),
    COL(""),
    OAVG(""),
    LVA(""),
    G7M(""),
    BGR(""),
    HRV(""),
    CYP(""),
    LTU(""),
    MKD(""),
    MLT(""),
    ROU(""),
    EU21(""),
    EA15(""),
    CHN(""),
    IND("");

    private String location;

    Locations(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
