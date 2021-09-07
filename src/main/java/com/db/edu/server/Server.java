package com.db.edu.server;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    private static String defaultHistoryFilePath = "history.log";

    private int port;
    private String historyFilePath;
    private ServerSocketConnectionController controller = new ServerSocketConnectionController();

    /**
     * Class Server implements a logic of Metaphora chat server.
     *
     * @param port - number of port server should be listening
     */
    Server(int port, String historyFilePath) {
        this.port = port;
        this.historyFilePath = historyFilePath;
    }

    public Server(int port) {
        this.port = port;
        this.historyFilePath = defaultHistoryFilePath;
    }

    /**
     * Start listening given port. Create new Socket when connection established.
     */
    public void start() throws IOException {
        new Thread(controller).start();
        try (final ServerSocket listener = new ServerSocket(port)) {
            while (true) {
                controller.pushNewConnection(new ServerSocketConnection(listener.accept(), historyFilePath));
            }
        }
    }
}
