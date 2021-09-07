package com.db.edu.utils;

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

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }
}
