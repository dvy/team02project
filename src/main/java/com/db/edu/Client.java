package com.db.edu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Client {

    public static final int MAX_LENGTH = 150;

    public void sendMessage(String message, DataOutputStream output) throws IOException {
        output.writeUTF(message); // protocol
        output.flush();
    }

    public void readAnswer(DataInputStream input) throws IOException {
        System.out.println(input.readUTF());
    }

    public boolean lengthCheck(String message) {
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

}
