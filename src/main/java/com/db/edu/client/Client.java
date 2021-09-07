package com.db.edu.client;

import com.db.edu.exceptions.EndOfSessionException;
import com.db.edu.exceptions.QueryProcessingException;
import com.db.edu.query.Query;
import com.db.edu.query.QueryFactory;
import com.db.edu.utils.NetworkController;

import java.io.*;
import java.util.Scanner;

/**
 * Connects to server and sends messages to other clients
 * Chat commands:
 *  /snd <message-text> - send message to other users of chat
 *  /hist - get history of all chat's messages
 */
public class Client {
    private boolean shouldWork = true;
    private NetworkController networkController;

    public Client(NetworkController networkController) {
        this.networkController = networkController;
    }

    /**
     * Implements the connection with server
     */
    public void socketConnectionRun() {
        try{
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
                processInput();
            }
            catch (EndOfSessionException e) {
                shouldWork = false;
            }
        }
    }

    void processInput() throws IOException {
        try {
            processQuery(readCommand());
        }
        catch (QueryProcessingException exception) {
            System.out.println(exception.getMessage());
        }
    }

    void processQuery(String message) throws IOException {
        Query query = QueryFactory.getQuery(message);
        networkController.getOutputStream().writeUTF(query.toString());
        networkController.getOutputStream().flush();
    }

    void listenServer() {
        Thread thread = new Thread(()->{
        while (true) {
            try {
                System.out.println(networkController.getInputStream().readUTF());
            } catch (IOException e) {
                break;
            }
        }
        });

        thread.setDaemon(true);
        thread.start();
    }
}
