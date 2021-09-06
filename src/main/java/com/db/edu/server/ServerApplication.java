package com.db.edu.server;

public class ServerApplication {
    public static void main(String[] args) {
        Server server = new Server(10_000, "history.log");
        server.start();
    }
}
