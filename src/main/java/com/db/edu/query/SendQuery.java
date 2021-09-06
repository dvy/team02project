package com.db.edu.query;

public class SendQuery implements Query {
    String message;
    public SendQuery(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "/snd" + message;
    }
}
