package com.db.edu.exceptions;

import java.io.IOException;

public class MessageReadException extends IOException {
    public MessageReadException(String message) {
        super(message);
    }
}
