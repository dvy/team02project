package com.db.edu.query;

import com.db.edu.exceptions.QueryProcessingException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendQuery implements Query {
    String message;
    String nickname;
    private static final int MAX_LENGTH = 150;
    public static final String MESSAGE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public SendQuery(String message) {
        if(message == null) throw new QueryProcessingException("Too few arguments: /snd command get non-empty string");
        if(message.length() > MAX_LENGTH) throw new QueryProcessingException("Too long message. Message length should be shorter");

        this.message = message;
    }

    public SendQuery(String message, String nickname) {
        this(message);
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(MESSAGE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        return "[" + dtf.format(now) + "] " + nickname + " : " + message.replaceFirst("/snd", "");
    }
}
