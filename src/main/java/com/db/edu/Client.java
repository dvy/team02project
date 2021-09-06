package com.db.edu;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        try (
                final Socket connection = new Socket("localhost", 10_000);
                final DataInputStream input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                final DataOutputStream output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()))
        ) {
            Scanner in = new Scanner(System.in);
            output.writeUTF(in.nextLine()); // protocol
            output.flush();

            System.out.println(input.readUTF());

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}