package com.db.edu.server;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    private int port;

    /**
     * Class Server implements a logic of Metaphora chat server.
     * @param port - number of port server should be listening
     */
    Server(int port) {
        this.port = port;
    }

    /**
     *  Start listening given port. Create new Socket when connection established.
     */
    public void start() {
        try ( final ServerSocket listener = new ServerSocket(port) ) {
            while (true) {
                ServerSocketConnection connect = new ServerSocketConnection(listener.accept());
                new Thread(connect).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
