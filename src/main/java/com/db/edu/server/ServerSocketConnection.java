package com.db.edu.server;

import com.db.edu.utils.History;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerSocketConnection implements Runnable {

    private static ConcurrentHashMap<SocketAddress, String> connections = new ConcurrentHashMap<>();
    private static ConcurrentLinkedQueue<String> messageBuffer = new ConcurrentLinkedQueue<>();

    private Socket connection;
    private DataInputStream networkInput;
    private DataOutputStream networkOutput;
    private SocketAddress address;

    private History history;
    public static Optional<String> getNextMessageFromBuffer() {
        return Optional.ofNullable(messageBuffer.poll());
    }

    public ServerSocketConnection(Socket connection, String filePath) throws IOException {
        this.connection = connection;
        this.networkInput = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
        this.networkOutput = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));

        this.history = new History(filePath);
        address = connection.getRemoteSocketAddress();
        if (address != null && !connections.containsKey(address)) {
            connections.put(address, address.toString());
        }
    }

    public void send(String message) {
        try {
            networkOutput.writeUTF(message);
            networkOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                final String message = networkInput.readUTF();
                if (message.startsWith("/snd ")) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String processedMessage = "[" + dtf.format(now) + "] " + connections.get(address) + " : " + message.replaceFirst("/snd ", "");
                    messageBuffer.add(processedMessage);
                    history.save(processedMessage);
                } else if (message.equals("/hist")) {
                    send(history.load());
                }
            } catch (SocketException e) {
                System.out.println("Socket " + address.toString() + " disconnected.");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return connection.isConnected();
    }
}
