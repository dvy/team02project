package com.db.edu.server;

import com.db.edu.exceptions.MessageReadException;
import com.db.edu.exceptions.MessageSendException;
import com.db.edu.exceptions.QueryProcessingException;
import com.db.edu.exceptions.SocketDisconnectedException;
import com.db.edu.query.Query;
import com.db.edu.query.QueryFactory;
import com.db.edu.utils.MessageProcessor;
import com.db.edu.utils.NetworkIOController;

import java.net.SocketAddress;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSocketConnection implements Runnable {
    private static final ConcurrentHashMap<SocketAddress, String> connectionsNickNames = new ConcurrentHashMap<>();

    public static Optional<String> getNickName(SocketAddress address){
        return Optional.ofNullable(connectionsNickNames.get(address));
    }

    private final NetworkIOController networkIOController;
    private final SocketAddress address;

    public ServerSocketConnection(NetworkIOController networkIOController, SocketAddress address) {
        this.networkIOController = networkIOController;
        this.address = address;

        if (address == null) throw new IllegalArgumentException("Socket address should not be null!");

        connectionsNickNames.put(this.address, this.address.toString());
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

    Query readNextQuery() throws QueryProcessingException, MessageReadException {
        return QueryFactory.getQuery(read(), connectionsNickNames.get(address));
    }

    @Override
    public void run() {
        while (true) {
            try {
                Query query;
                try {
                    query = readNextQuery();
                } catch (QueryProcessingException exception) {
                    send(exception.getMessage());
                    continue;
                }
                String toSend = MessageProcessor.processMessage(query);
                if (toSend == null || toSend.isEmpty()) {
                    continue;
                }
                send(toSend);
            } catch (MessageSendException e) {
                System.out.println("Socket " + address.toString() + " : Can't send message.");
            } catch (MessageReadException e) {
                throw new SocketDisconnectedException("Socket " + address.toString() + " : disconnected.");
            }
        }
    }
}
