package com.db.edu.exceptions;

public class SocketDisconnectedException extends RuntimeException {
    public SocketDisconnectedException(String message) {
        super(message);
    }
}
