package com.db.edu.server;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    private int port;

    Server(int port) {
        this.port = port;
    }

    void start() {
        try ( final ServerSocket listener = new ServerSocket(port) ) {
            ServerSocketConnection connect = new ServerSocketConnection(listener.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
