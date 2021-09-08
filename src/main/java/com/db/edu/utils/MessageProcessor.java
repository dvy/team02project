package com.db.edu.utils;


import com.db.edu.exceptions.MessageSendException;
import com.db.edu.query.HistoryQuery;
import com.db.edu.query.Query;
import com.db.edu.query.SendQuery;

import java.net.Socket;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageProcessor {
    private final static String sendPrefix = "/snd ";
    private final static String historyPrefix = "/hist";
    private static History history;
    private static ConcurrentLinkedQueue<String> messageBuffer = new ConcurrentLinkedQueue<>();


    public MessageProcessor() {
    }

    public static Optional<String> getNextMessageFromBuffer() {
        return Optional.ofNullable(messageBuffer.poll());
    }

    public static void setHistory(String filePath) {
        history = new History(filePath);
    }

    public String processMessage(Query query) {
        if (query instanceof SendQuery) {
            return processMessage((SendQuery) query);
        } else if (query instanceof HistoryQuery) {
            return processMessage((HistoryQuery) query);
        } else {
            return null;
        }
    }

    public String processMessage(SendQuery query) {
        System.out.println("send");
        String message = query.toString();
        messageBuffer.add(message);
        history.save(message);
        return null;
    }

    public String processMessage(HistoryQuery query) {
        System.out.println("hist");
        return history.load();
    }

}
