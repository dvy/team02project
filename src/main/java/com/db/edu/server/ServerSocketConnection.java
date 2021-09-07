package com.db.edu.server;

import com.db.edu.exceptions.MessageReadException;
import com.db.edu.exceptions.MessageSendException;
import com.db.edu.utils.History;
import com.db.edu.utils.NetworkIOController;

import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerSocketConnection implements Runnable {
    private final static String sendPrefix = "/snd ";
    private final static String historyPrefix = "/hist";

    private static ConcurrentHashMap<SocketAddress, String> connections = new ConcurrentHashMap<>();
    private static ConcurrentLinkedQueue<String> messageBuffer = new ConcurrentLinkedQueue<>();

    private NetworkIOController networkIOController;
    private SocketAddress address;

    private History history;
    public static Optional<String> getNextMessageFromBuffer() {
        return Optional.ofNullable(messageBuffer.poll());
    }

    public ServerSocketConnection(NetworkIOController networkIOController, SocketAddress address, String filePath) {
        this.networkIOController = networkIOController;

        this.history = new History(filePath);
        this.address = address;

        if (address != null && !connections.containsKey(address)) {
            connections.put(this.address, this.address.toString());
        }
    }

    public SocketAddress getAddress() {
        return address;
    }

    public void send(String message) throws MessageSendException {
        networkIOController.write(message);
    }

    public String read() throws MessageReadException {
        return networkIOController.read();
    }

    String formatMessage(String pattern, String message){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        return "[" + dtf.format(now) + "] " + connections.get(address) + " : " + message.replaceFirst(sendPrefix, "");
    }

    void processMessageToSend(String message){
        messageBuffer.add(message);
        history.save(message);
    }

    void processHistoryMessage() throws MessageSendException {
        send(history.load());
    }

    void processMessage(String message) throws MessageSendException {
        if (message.startsWith(sendPrefix)) processMessageToSend(formatMessage("yyyy/MM/dd HH:mm:ss", message));
        if (message.equals(historyPrefix)) processHistoryMessage();
    }

    @Override
    public void run() {
        while (true) {
            try {
                processMessage(read());
            } catch (MessageSendException e) {
                System.out.println("Socket " + address.toString() + " : Can't send message.");
            } catch (MessageReadException e) {
                System.out.println("Socket " + address.toString() + " : Can't read message.");
            }
        }
    }
}
