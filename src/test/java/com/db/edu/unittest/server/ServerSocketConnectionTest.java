package com.db.edu.unittest.server;

import com.db.edu.server.ServerSocketConnection;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ServerSocketConnectionTest {
    @Test
    public void shouldReturnNullStringWhenBufferIsEmpty() throws IOException {
        assertFalse(ServerSocketConnection.getNextMessageFromBuffer().isPresent());
    }

    @Test
    public void shouldThrowExceptionWhenSocketIsNotConnected() throws IOException {
        Socket socket = new Socket();
        assertThrows(SocketException.class, () -> new ServerSocketConnection(socket, ""));
    }
}

