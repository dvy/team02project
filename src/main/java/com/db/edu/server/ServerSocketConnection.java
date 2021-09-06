package com.db.edu.server;

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
    private static ConcurrentLinkedQueue<String> history = new ConcurrentLinkedQueue<>();
    private static ConcurrentLinkedQueue<String> messageBuffer = new ConcurrentLinkedQueue<>();

    private Socket connection;
    private DataInputStream input;
    private DataOutputStream output;
    private SocketAddress address;

    public static Optional<String> getNextMessageFromBuffer() {
        return Optional.ofNullable(messageBuffer.poll());
    }

    public ServerSocketConnection(Socket connection) throws IOException {
        this.connection = connection;
        this.input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
        this.output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));

        address = connection.getRemoteSocketAddress();
        if (address != null && !connections.containsKey(address)) {
            connections.put(address, address.toString());
        }
    }

    public void send(String message) {
        try {
            output.writeUTF(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                final String message = input.readUTF();
                if (message.startsWith("/snd ")) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();

                    String processedMessage = "[" + dtf.format(now) + "] " + connections.get(address) + " : " + message.replaceFirst("/snd ", "");
                    messageBuffer.add(processedMessage);
                    history.add(processedMessage);

                } else if (message.equals("/hist")) {
                    String historyMessage = new String();
                    for (String element : history) {
                        historyMessage += element + System.lineSeparator();
                    }

                    send(historyMessage);
                } else {
                    send("Not supported operation: " + message.substring(0, message.indexOf(" ")) + " is not recognised");
                }
            } catch (SocketException e) {
                System.out.println("Socket " + address.toString() + " disconnected.");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
