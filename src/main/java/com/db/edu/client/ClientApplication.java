package com.db.edu.client;

import com.db.edu.utils.NetworkController;

import java.io.IOException;

/**
 * Runs application for client part of Chat Application.
 * @see Client
 */
public class ClientApplication {
    public static void main(String[] args) {
        try {
            NetworkController networkController = new NetworkController("localhost", 10_000);
            Client client = new Client(networkController);
            client.socketConnectionRun();
        } catch (IOException exception) {
            System.out.println("Server is not available. Please try again later.");
        }

    }
}