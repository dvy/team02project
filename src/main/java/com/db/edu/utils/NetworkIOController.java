package com.db.edu.utils;

import com.db.edu.exceptions.MessageReadException;
import com.db.edu.exceptions.MessageSendException;

import java.io.*;
import java.net.Socket;

public class NetworkIOController implements Closeable {
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public NetworkIOController(Socket connection) throws IOException {
        this.inputStream = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
        this.outputStream = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
    }
    @Override
    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
    }

    public void write(String message) throws MessageSendException {
        try {
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            throw new MessageSendException("Couldn't send data");
        }
    }

    public String read() throws MessageReadException {
        try {
            return inputStream.readUTF();
        } catch (IOException e) {
            throw new MessageReadException("Couldn't get data");
        }
    }
}
