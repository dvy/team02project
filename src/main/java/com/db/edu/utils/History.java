package com.db.edu.utils;


import java.io.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class History {
    private final String historyFilePath;
    private final ReentrantReadWriteLock historyLock = new ReentrantReadWriteLock();

    public History(String filePath) {
        this.historyFilePath = filePath;
    }

    public void save(String processedMessage) {
        historyLock.writeLock().lock();
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(historyFilePath, true))) {
            fileWriter.append(processedMessage)
                    .append(System.lineSeparator());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            historyLock.writeLock().unlock();
        }

    }

    public String load(){
        String historyMessage = "";
        historyLock.readLock().lock();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(historyFilePath))){
            historyMessage = fileReader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            historyLock.readLock().unlock();
        }

        return historyMessage.isEmpty() ? "History is empty" : historyMessage;
    }
}

