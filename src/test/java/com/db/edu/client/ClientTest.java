package com.db.edu.client;

import com.db.edu.SysoutCaptureAndAssertionAbility;
import com.db.edu.exceptions.QueryProcessingException;
import com.db.edu.utils.History;
import com.db.edu.utils.NetworkIOController;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientTest implements SysoutCaptureAndAssertionAbility {
    File file;
    String filePath = "testCases";

    @BeforeEach
    public void setUpSystemOut() {
        resetOut();
        captureSysout();
        file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        resetOut();
        file.delete();
    }

    private void writeToFile(String... strings) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String string : strings) {
                fileWriter.append(string + System.lineSeparator());
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shouldNotWriteTOServerWhenExitCommand() throws QueryProcessingException, IOException {
        writeToFile("/exit");
        Scanner scanner = new Scanner(new FileReader("testCases"));
        NetworkIOController networkIOController = mock(NetworkIOController.class);
        Client client = new Client(networkIOController, scanner);
        client.sendToServer();
        verify(networkIOController, times(0)).write(any());
    }

    @Test
    public void shouldWriteToServerWhenNotExitCommand() throws QueryProcessingException, IOException {
        writeToFile("/snd qwerty", "/snd qwerty", "/exit");
        Scanner scanner = new Scanner(new FileReader("testCases"));
        NetworkIOController networkIOController = mock(NetworkIOController.class);
        Client client = new Client(networkIOController, scanner);
        client.sendToServer();
        verify(networkIOController, times(2)).write(any());
    }

    @Test
    public void shouldNotWriteToServerWhenMessageIsLong() throws QueryProcessingException, IOException {
        writeToFile("/snd toolongmessagetoolongmessagetoolongmessagetoolongmessagetoolongmessagetoolongmessagetoolongmessagetoolongmessagetoolongmessagetoolongmessagetoolongmessage", "/exit");
        Scanner scanner = new Scanner(new FileReader("testCases"));
        NetworkIOController networkIOController = mock(NetworkIOController.class);
        Client client = new Client(networkIOController, scanner);
        client.sendToServer();
        verify(networkIOController, times(0)).write(any());
    }
}
