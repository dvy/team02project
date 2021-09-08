package com.db.edu.server;

import com.db.edu.utils.History;
import com.db.edu.utils.MessageProcessor;
import com.db.edu.utils.NetworkIOController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private final int port;
    private final ServerSocketConnectionController controller;

    /**
     * Class Server implements a logic of Metaphora chat server.
     *
     * @param port - number of port server should be listening
     */
    public Server(int port, String historyFilePath, ServerSocketConnectionController controller) {
        this.controller = controller;
        this.port = port;
        MessageProcessor.setHistory(new History(historyFilePath));
    }

    private volatile boolean shouldExit;

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
                        if (s.equals("/exit")) {
                            controllerThread.interrupt();
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
                Socket acceptedSocket = listener.accept();
                NetworkIOController networkIOController = new NetworkIOController(acceptedSocket);
                try {
                    controller.pushNewConnection(new ServerSocketConnection(networkIOController,
                            acceptedSocket.getRemoteSocketAddress()));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
