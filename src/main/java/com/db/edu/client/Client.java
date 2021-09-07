package com.db.edu.client;

import com.db.edu.exceptions.QueryProcessingException;
import com.db.edu.query.Query;
import com.db.edu.query.QueryFactory;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Connects to server and sends messages to other clients
 * Chat commands:
 *  /snd <message-text> - send message to other users of chat
 *  /hist - get history of all chat's messages
 */
public class Client {
    private final String host;
    private final int port;

    /**
     * @param host - host connection number
     * @param port - port connection number
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Implements the connection with server
     */
    public void socketConnectionRun() {
        try (
                final Socket connection = new Socket(host, port);
                final DataInputStream input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                final DataOutputStream output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()))
        ) {
            this.listenServer(input);
            while (true) {
                Scanner in = new Scanner(System.in);
                String message = in.nextLine();

                try {
                    processQuery(message, output);
                }
                catch (QueryProcessingException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void processQuery(String message, DataOutputStream output) throws IOException {
        Query query = QueryFactory.GetQuery(message);
        output.writeUTF(query.toString());
        output.flush();
    }

    public void listenServer(DataInputStream input) throws IOException {
        Thread thread = new Thread(()->{
        while (true) {
            try {
                System.out.println(input.readUTF());
            } catch (IOException e) {
                System.out.println(e.getMessage());
                break;
            }
        }});

        thread.setDaemon(true);
        thread.start();
    }
}
