package com.db.edu.server;

import com.db.edu.exceptions.SocketDisconnectedException;
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

    public SocketAddress getAddress() {
        return address;
    }

    public void send(String message) throws IOException {
        networkOutput.writeUTF(message);
        networkOutput.flush();
    }

    String formatMessage(String pattern, String message){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        return "[" + dtf.format(now) + "] " + connections.get(address) + " : " + message.replaceFirst("/snd ", "");
    }

    void processMessageToSend(String message){
        messageBuffer.add(message);
        history.save(message);
    }

    @Override
    public void run() {
        while (isConnected()) {
            try {
                final String message = networkInput.readUTF();
                if (message.startsWith("/snd ")) {
                    processMessageToSend(formatMessage("yyyy/MM/dd HH:mm:ss", message));
                } else if (message.equals("/hist")) {
                    send(history.load());
                }
            } catch (SocketException e) {
                System.out.println("Socket " + address.toString() + " disconnected.");
                throw new SocketDisconnectedException(address.toString());
            } catch (IOException e) {
                System.out.println("Socket " + address.toString() + " exception while reading.");
                return;
            }
        }
    }

    public boolean isConnected() {
        return connection.isConnected();
    }
}
