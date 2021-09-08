package com.db.edu.client;

import com.db.edu.exceptions.EndOfSessionException;
import com.db.edu.exceptions.LongMessageException;
import com.db.edu.utils.NetworkIOController;

import java.io.*;
import java.util.Scanner;

/**
 * Connects to server and sends messages to other clients
 * Chat commands:
 * /snd <message-text> - send message to other users of chat
 * /hist - get history of all chat's messages
 */
public class Client {
    private final int MAX_LENGTH = 150;
    private boolean shouldWork = true;
    private NetworkIOController networkIOController;

    //Scanner
    public Client(NetworkIOController networkIOController) {
        this.networkIOController = networkIOController;
    }

    /**
     * Implements the connection with server
     */
    public void socketConnectionRun() {
        try {
            listenServer();
            sendToServer();
        } catch (IOException e) {
            System.out.println("The server is not responding. Please restart chat.");
        }
    }

    String readCommand() {
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    void sendToServer() throws IOException {
        while (shouldWork) {
            try {
                String message = readCommand();
                if ("/exit".equals(message)) {
                    shouldWork = false;
                    continue;
                }
                if (message.length() > MAX_LENGTH) {
                    throw new LongMessageException();
                }
                networkIOController.write(message);
            } catch (LongMessageException e) {
                System.out.println("Message too long. Max message length is 150");
            }
        }
    }

    void listenServer() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println(networkIOController.read());
                } catch (IOException e) {
                    break;
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
}
