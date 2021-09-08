package com.db.edu.utils;

import com.db.edu.exceptions.HistorySaveLoadException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class History {
    private final String filePath;
    private final ReentrantReadWriteLock historyLock;
    private final BufferedWriter fileWriter;
    private final BufferedReader fileReader;

    public History(String filePath) {
        this.filePath = filePath;
        this.historyLock = new ReentrantReadWriteLock();
        try {
            this.fileWriter = new BufferedWriter(new FileWriter(filePath, true));
            this.fileReader = new BufferedReader(new FileReader(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new HistorySaveLoadException(
                    "Can't save your message to file: buffer is not available.");
        }
    }

    public void save(String processedMessage) {
        historyLock.writeLock().lock();
        try {
            fileWriter.append(processedMessage).append(System.lineSeparator());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new HistorySaveLoadException(
                    "Can't save your message to file: writer is not available.");
        } finally {
            historyLock.writeLock().unlock();
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new HistorySaveLoadException(
                        "Can't save your message to file: can't close writer.");
            }
        }
    }

    public String load() {
        String historyMessage = "";
        historyLock.readLock().lock();
        historyMessage = fileReader.lines().collect(Collectors.joining(System.lineSeparator()));
        historyLock.readLock().unlock();
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new HistorySaveLoadException(
                    "Can't load message history: can't close reader.");
        }
        return historyMessage.isEmpty() ? "History is empty" : historyMessage;
    }
}

