package com.db.edu.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

import static com.db.edu.server.ServerSocketConnection.getNextMessageFromBuffer;

public class ServerSocketConnectionController implements Runnable {

    LinkedList<ServerSocketConnection> connections = new LinkedList<>();

    @Override
    public void run() {
        while (true) {
            Optional<String> nextMessage = getNextMessageFromBuffer();
            if (nextMessage.isPresent()) {
                for (ServerSocketConnection connection : connections) {
                    if(!connection.isConnected()) {
                        connections.remove(connection);
                        continue;
                    }

                    try {
                        connection.send(nextMessage.get());
                    } catch (IOException e) {
                        System.out.println("Can't send message to " + connection.getAddress() + " : IO error");
                    }
                }
            }
        }
    }

    public void pushNewConnection(ServerSocketConnection connection) {
        connections.push(connection);
        new Thread(connection).start();
    }
}
