package com.db.edu.unittest;

import com.db.edu.utils.History;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryTest {
    File file;
    History history;
    String filePath = "historyTest";
    String message1 = "Message1";
    String message2 = "Message2";

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
        history.save(message1);
        history.save(message2);
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                fileContent.append(br.readLine());
                fileContent.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(message1+"\n"+message2+"\n", fileContent.toString());
    }

    @Test
    public void loadFromFilledHistory() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(message1);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String historyContent = history.load();
        assertEquals(message1, historyContent);
    }

    @Test
    public void loadFromEmptyHistory() {
        String historyContent = history.load();
        assertEquals("History is empty", historyContent);
    }
}
