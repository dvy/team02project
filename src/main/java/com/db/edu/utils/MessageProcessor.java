package com.db.edu.utils;

import com.db.edu.query.HistoryQuery;
import com.db.edu.query.Query;
import com.db.edu.query.SendQuery;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageProcessor {
    private static History history;
    private static final ConcurrentLinkedQueue<String> messageBuffer = new ConcurrentLinkedQueue<>();

    private MessageProcessor() {
    }

    public static Optional<String> getNextMessageFromBuffer() {
        return Optional.ofNullable(messageBuffer.poll());
    }

    public static void setHistory(History hist) {
        history = hist;
    }

    public static String processMessage(Query query) {
        if (query instanceof SendQuery) {
            return processMessage((SendQuery) query);
        } else if (query instanceof HistoryQuery) {
            return processMessage((HistoryQuery) query);
        } else {
            return null;
        }
    }

    public static String processMessage(SendQuery query) {
        String message = query.toString();
        messageBuffer.add(message);
        history.save(message);
        return null;
    }

    public static String processMessage(HistoryQuery query) {
        return history.load();
    }

}
