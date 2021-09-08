package com.db.edu.server;

import com.db.edu.exceptions.MessageReadException;
import com.db.edu.exceptions.MessageSendException;
import com.db.edu.exceptions.QueryProcessingException;
import com.db.edu.exceptions.SocketDisconnectedException;
import com.db.edu.query.Query;
import com.db.edu.query.QueryFactory;
import com.db.edu.utils.History;
import com.db.edu.utils.MessageProcessor;
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

    private NetworkIOController networkIOController;
    private SocketAddress address;

    public ServerSocketConnection(NetworkIOController networkIOController, SocketAddress address) {
        this.networkIOController = networkIOController;

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
        String str = networkIOController.read();
        System.out.println(str);
        return str;
    }



    @Override
    public void run() {
        while (true) {
            try {
                MessageProcessor messageProcessor = new MessageProcessor();
                Query query;
                try {
                    query = QueryFactory.getQuery(read(), connections.get(address));
                } catch (QueryProcessingException exception) {
                    send(exception.getMessage());
                    continue;
                }
                String toSend = messageProcessor.processMessage(query);
                if(toSend == null || toSend.isEmpty()) {
                    System.out.println("str is null");
                    continue;
                }
                System.out.println(toSend);
                send(toSend);
            } catch (MessageSendException e) {
                System.out.println("Socket " + address.toString() + " : Can't send message.");
            }
            catch (MessageReadException e) {
                throw new SocketDisconnectedException("Socket " + address.toString() + " : disconnected.");
            }
        }
    }
}
