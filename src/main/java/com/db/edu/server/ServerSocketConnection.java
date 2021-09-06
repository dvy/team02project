package com.db.edu.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;

public class ServerSocketConnection {

    private static HashMap<SocketAddress, String> connections = new HashMap<>();
    private static LinkedList<String> history = new LinkedList<>();

    private Socket connection;
    private DataInputStream input;
    private DataOutputStream output;
    private SocketAddress address;

    public ServerSocketConnection(Socket connection) {
        try {
            this.connection = connection;
            this.input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
            this.output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));

            address = connection.getRemoteSocketAddress();
            if (address != null && !connections.containsKey(address)) {
                connections.put(address, address.toString());
            }

            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() throws IOException {
        while (true) {
            final String message = input.readUTF();
            if (message.startsWith("/snd ")) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                String processedMessage = "[" + dtf.format(now) + "] " + connections.get(address) + " : " + message.replaceFirst("/snd ", "");
                output.writeUTF(processedMessage);
                output.flush();

                history.add(processedMessage);

            } else if (message.equals("/hist")) {
                String historyMessage = new String();
                for (String element : history) {
                    historyMessage += element + System.lineSeparator();
                }

                output.writeUTF(historyMessage);
                output.flush();
            } else {
                output.writeUTF("Not supported operation: " + message.substring(0, message.indexOf(" ")) + " is not recognised");
                output.flush();
            }
        }
    }
}
