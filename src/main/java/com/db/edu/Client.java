package com.db.edu;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int MAX_LENGTH = 150;

    public static void main(String[] args) {

        try (
                final Socket connection = new Socket("localhost", 10_000);
                final DataInputStream input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                final DataOutputStream output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()))
        ) {
            while (true) {
                Scanner in = new Scanner(System.in);
                String message = in.nextLine();

                if (message.startsWith("/snd ")) {
                    if (message.length() > MAX_LENGTH + 5) {
                        System.out.println("Message length should be less than 150 symbols");
                    } else {
                        output.writeUTF(message); // protocol
                        output.flush();

                        System.out.println(input.readUTF());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}