package com.db.edu.query;

public enum QueryType {
    SEND("/snd"),
    HISTORY("/hist"),
    CHANGE_ID("/chid");

    private String type;
    QueryType(String type) {
        this.type = type;
    }

}

