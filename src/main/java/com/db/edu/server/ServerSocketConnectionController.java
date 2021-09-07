package com.db.edu.server;

import com.db.edu.exceptions.MessageWriteException;
import com.db.edu.exceptions.SocketDisconnectedException;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.db.edu.server.ServerSocketConnection.getNextMessageFromBuffer;

public class ServerSocketConnectionController implements Runnable {
    ConcurrentLinkedQueue<ServerSocketConnection> connections = new ConcurrentLinkedQueue<>();

    Thread.UncaughtExceptionHandler handler = (th, ex) -> {
        if (ex instanceof SocketDisconnectedException) {
            connections.removeIf(a -> ex.getMessage().equals(a.getAddress().toString()));
        }
    };

    @Override
    public void run() {
        while (true) {
            Optional<String> nextMessage = getNextMessageFromBuffer();
            if (nextMessage.isPresent()) {
                for (ServerSocketConnection connection : connections) {
                    try {
                        connection.send(nextMessage.get());
                    } catch (MessageWriteException e) {
                        System.out.println("Can't send message to " + connection.getAddress());
                    }
                }
            }
        }
    }

    public void pushNewConnection(ServerSocketConnection connection) {
        connections.add(connection);
        Thread t = new Thread(connection);
        t.setUncaughtExceptionHandler(handler);
        t.setDaemon(true);
        t.start();
    }
}
