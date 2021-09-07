package com.db.edu.utils;

import com.db.edu.client.Client;

public class ClientConnection {
    private final String host;
    private final int port;
    ClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
