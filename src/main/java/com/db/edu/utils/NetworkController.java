package com.db.edu.utils;

import java.io.*;
import java.net.Socket;

public class NetworkController implements Closeable {
    private Socket connection;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public NetworkController(String host, int port) throws IOException {
        this.connection = new Socket(host, port);
        this.inputStream = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
        this.outputStream = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
    }
    @Override
    public void close() throws IOException {
        connection.close();
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
