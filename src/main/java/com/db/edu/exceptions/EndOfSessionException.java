package com.db.edu.exceptions;

public class EndOfSessionException extends RuntimeException{
    public EndOfSessionException(String message) {
        super(message);
    }
}
