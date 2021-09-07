package com.db.edu.client;

import com.db.edu.utils.NetworkIOController;

import java.io.IOException;
import java.net.Socket;

/**
 * Runs application for client part of Chat Application.
 * @see Client
 */
public class ClientApplication {
    public static void main(String[] args) {
        try {
            Socket connection = new Socket("localhost", 10_000);
            NetworkIOController networkIOController = new NetworkIOController(connection);
            Client client = new Client(networkIOController);
            client.socketConnectionRun();
        } catch (IOException exception) {
            System.out.println("Couldn't connect to server. Please try again later.");
        }

    }
}