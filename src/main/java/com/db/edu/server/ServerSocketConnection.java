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
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerSocketConnection implements Runnable {

    private static ConcurrentHashMap<SocketAddress, String> connections = new ConcurrentHashMap<>();
    private static ConcurrentLinkedQueue<String> messageBuffer = new ConcurrentLinkedQueue<>();

    private Socket connection;
    private DataInputStream networkInput;
    private DataOutputStream networkOutput;
    private SocketAddress address;

    private ReentrantReadWriteLock historyLock = new ReentrantReadWriteLock();
    private String historyFilePath;

    public static Optional<String> getNextMessageFromBuffer() {
        return Optional.ofNullable(messageBuffer.poll());
    }

    public ServerSocketConnection(Socket connection, String filePath) throws IOException {
        this.connection = connection;
        this.networkInput = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
        this.networkOutput = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));

        this.historyFilePath = filePath;

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

                    historyLock.writeLock().lock();
                    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(historyFilePath, true));
                    fileWriter.append(processedMessage + System.lineSeparator());
                    fileWriter.flush();
                    historyLock.writeLock().unlock();

                } else if (message.equals("/hist")) {
                    StringBuilder historyMessage = new StringBuilder();

                    historyLock.readLock().lock();
                    BufferedReader fileReader = new BufferedReader(new FileReader(historyFilePath));
                    String line;
                    while ((line = fileReader.readLine()) != null) {
                        historyMessage.append(line).append(System.lineSeparator());
                    }
                    historyLock.readLock().unlock();

                    send(historyMessage.toString());
                } else {
                    send("Not supported operation: " + message.substring(0, message.indexOf(' ')) + " is not recognised");
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
