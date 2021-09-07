package com.db.edu.server;

import java.io.*;
import java.net.ServerSocket;
import java.util.Scanner;

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

    volatile boolean shouldExit;
    /**
     * Start listening given port. Create new Socket when connection established.
     */
    public void start() throws IOException {
        Thread controllerThread = new Thread(controller);
        controllerThread.setDaemon(true);
        controllerThread.start();

        try (final ServerSocket listener = new ServerSocket(port)) {
            Thread t = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true){
                    if (scanner.hasNext()) {
                        String s = scanner.next();
                        if (s.equals("/quit")) {
                            controllerThread.interrupt();
                            try {
                                listener.close();
                            } catch (IOException e) {
                                System.out.println("Can't close server");
                            }
                            System.out.println("Server shut down.");
                            shouldExit = true;
                            return;
                        }
                    }
                }
            });

            t.setDaemon(true);
            t.start();

            while (!shouldExit) {
                controller.pushNewConnection(new ServerSocketConnection(listener.accept(), historyFilePath));
            }
        }
    }
}
