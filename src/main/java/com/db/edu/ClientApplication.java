package com.db.edu;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientApplication {

    public static void main(String[] args) {
        Client client = new Client();
        try (
                final Socket connection = new Socket("localhost", 10_000);
                final DataInputStream input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                final DataOutputStream output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()))
        ) {
            while (true) {
                Scanner in = new Scanner(System.in);
                String message = in.nextLine();

                if (!client.lengthCheck(message)) {
                        continue;
                }
                client.sendMessage(message, output);
                client.readAnswer(input);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}