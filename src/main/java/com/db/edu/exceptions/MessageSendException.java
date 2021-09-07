package com.db.edu.exceptions;

import java.io.IOException;

public class MessageSendException extends IOException {
    public MessageSendException(String message) {
        super(message);
    }
}
