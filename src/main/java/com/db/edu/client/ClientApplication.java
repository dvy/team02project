package com.db.edu.client;

import com.db.edu.utils.NetworkIOController;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Runs application for client part of Chat Application.
 * @see Client
 */
public class ClientApplication {
    public static void main(String[] args) {
        try {
            Socket connection = new Socket("localhost", 10_000);
            NetworkIOController networkIOController = new NetworkIOController(connection);
            Scanner scanner = new Scanner(System.in);
            Client client = new Client(networkIOController, scanner);
            client.socketConnectionRun();
        } catch (IOException exception) {
            System.out.println("Couldn't connect to server. Please try again later.");
        }

    }
}