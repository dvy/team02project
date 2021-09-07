package com.db.edu;

import com.db.edu.utils.History;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryTest {
    File file;
    History history;
    String filePath = "historyTest";
    String message = "Message";

    @BeforeEach
    public void setUp() {
        file = new File(filePath);
        history = new History(filePath);
    }

    @AfterEach
    public void reset() {
        file.delete();
    }

    @Test
    public void saveToHistory() {
        history.save(message);
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready())
                fileContent.append(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(message, fileContent.toString());
    }

    @Test
    public void loadFromFilledHistory() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(message);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String historyContent = history.load();
        assertEquals(message, historyContent);
    }

    @Test
    public void loadFromEmptyHistory() {
        String historyContent = history.load();
        assertEquals("History is empty", historyContent);
    }
}
