package com.db.edu.query;

import com.db.edu.exceptions.QueryProcessingException;

public class ChangeIDQuery implements Query {
    String message;

    public ChangeIDQuery(String message) {
        if(message == null) throw new QueryProcessingException("Too few arguments: /chid command get non-empty string");
        this.message = message;
    }

    @Override
    public String toString() {
        return "/chid " + message;
    }
}
