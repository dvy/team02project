package com.db.edu.query;

import com.db.edu.exceptions.QueryProcessingException;

public class HistoryQuery implements Query {
    public HistoryQuery(String message) {
        if(message != null) throw new QueryProcessingException("Too many arguments: /hist command does not get arguments");
    }

    @Override
    public String toString() {
        return "/hist";
    }
}
