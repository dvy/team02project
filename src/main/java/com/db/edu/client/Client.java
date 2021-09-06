package com.db.edu.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int MAX_LENGTH = 150;
    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

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

                if (!this.lengthCheck(message)) {
                    continue;
                }
                this.sendMessage(message, output);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void sendMessage(String message, DataOutputStream output) throws IOException {
        output.writeUTF(message);
        output.flush();
    }

    private void listenServer(DataInputStream input) throws IOException {
        Thread thread = new Thread(()->{
        while (true) {
            try {
                System.out.println(input.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }});
        thread.setDaemon(true);
        thread.start();
    }

    private boolean lengthCheck(String message) {
        if (message.startsWith("/snd ")) {
            if (message.length() > MAX_LENGTH + 5) {
                System.out.println("Message length should be less than 150 symbols");
                return false;
            }
        }
        return true;
    }
}
