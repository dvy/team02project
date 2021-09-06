package com.db.edu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class implement string part of Chat Application.
 * Processes incoming requests from clients
 */
public class Server {
    public static void main(String[] args) {

        HashMap<SocketAddress, String> connections = new HashMap<>();
        LinkedList<String> history = new LinkedList<>();

        try (final ServerSocket listener = new ServerSocket(10_000)) {
            try (
                    final Socket connection = listener.accept();
                    final DataInputStream input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                    final DataOutputStream output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()))
            ) {
                SocketAddress address = connection.getRemoteSocketAddress();
                if (address != null && !connections.containsKey(address)) {
                    connections.put(address, address.toString());
                }

                while (true) {
                    final String message = input.readUTF();

                    if (message.startsWith("/snd")) {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String processedMessage = "[" + dtf.format(now) + "] " + connections.get(address) + " : " + message.replaceFirst("/snd ", "");
                        output.writeUTF(processedMessage);
                        output.flush();

                        history.add(processedMessage);

                    } else if (message.startsWith("/hist")) {
                        String historyMessage = new String();
                        for (var element : history) {
                            historyMessage += element + System.lineSeparator();
                        }

                        output.writeUTF(historyMessage);
                        output.flush();
                    } else {
                        output.writeUTF("Not supported operation: " + message.substring(0, message.indexOf(" ")) + " is not recognised");
                        output.flush();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace(System.err);
            }

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
