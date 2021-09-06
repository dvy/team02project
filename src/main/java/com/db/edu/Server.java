package com.db.edu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {
    public static void main(String[] args) {
        try (final ServerSocket listener = new ServerSocket(10_000)) {
            try (
                    final Socket connection = listener.accept();
                    final DataInputStream input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                    final DataOutputStream output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()))
            ) {

                while (true) {
                    final String message = input.readUTF();
                    if ( message.startsWith("\\snd") ) {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String processedMessage = now.toString() + " UserName " + message.replaceFirst("\\snd", "");
                        output.writeChars(processedMessage);
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
