package com.db.edu.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static final int MAX_LENGTH = 150;
    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void sendMessage(String message, DataOutputStream output) throws IOException {
        output.writeUTF(message); // protocol
        output.flush();
    }

    private void readAnswer(DataInputStream input) throws IOException {
        System.out.println(input.readUTF());
    }

    private boolean lengthCheck(String message) {
        if (message.startsWith("/snd ")) {
            if (message.length() > MAX_LENGTH + 5) {
                System.out.println("Message length should be less than 150 symbols");
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public void socketConnectionRun() {
        try (
                final Socket connection = new Socket(host, port);
                final DataInputStream input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                final DataOutputStream output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()))
        ) {
            while (true) {
                Scanner in = new Scanner(System.in);
                String message = in.nextLine();

                if (!this.lengthCheck(message)) {
                    continue;
                }
                this.sendMessage(message, output);
                this.readAnswer(input);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

}
