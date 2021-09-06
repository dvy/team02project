package com.db.edu.client;

public class ClientApplication {
    private static final Client client = new Client("localhost", 10_000);

    public static void main(String[] args) {
        client.socketConnectionRun();
    }
}