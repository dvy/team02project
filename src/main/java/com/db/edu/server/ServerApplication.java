package com.db.edu.server;

import java.io.IOException;

public class ServerApplication {
    public static void main(String[] args) {
        ServerSocketConnectionController controller = new ServerSocketConnectionController();
        Server server = new Server(10_000, "history.log", controller);

        try {
            server.start();
        } catch (IOException e) {
            System.out.println("Troubles with server");
        }
    }
}
