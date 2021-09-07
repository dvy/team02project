package com.db.edu.utils;

import com.db.edu.exceptions.MessageReadException;
import com.db.edu.exceptions.MessageWriteException;

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

    public void write(String message) throws MessageWriteException {
        try {
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            throw new MessageWriteException();
        }
    }

    public String read() throws MessageReadException {
        try {
            return inputStream.readUTF();
        } catch (IOException e) {
            throw new MessageReadException();
        }
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }
}
