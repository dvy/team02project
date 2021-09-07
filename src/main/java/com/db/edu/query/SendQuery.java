package com.db.edu.query;

import com.db.edu.exceptions.QueryProcessingException;

public class SendQuery implements Query {
    String message;
    private static final int MAX_LENGTH = 150;

    public SendQuery(String message) {
        if(message == null) throw new QueryProcessingException("Too few arguments: /snd command get non-empty string");
        if(message.length() > MAX_LENGTH) throw new QueryProcessingException("Too long message. Message length should be shorter");

        this.message = message;
    }

    @Override
    public String toString() {
        return "/snd " + message;
    }
}
