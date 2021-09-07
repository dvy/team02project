package com.db.edu.unittest;

import com.db.edu.client.Client;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.mockito.Mockito.*;
/*
public class ClientTest {

    private final Client client = new Client("localhost", 10_000);
    private DataOutputStream output = mock(DataOutputStream.class);

    @Test
    public void weCanSendStringMessageToServerIfItsLengthIsLessThan150() throws IOException {
        String message = "Hello";
        if (client.lengthCheck(message)) {
            client.processQuery(message, output);
        }

        verify(output, times(1)).writeUTF("Hello");
        verify(output, times(1)).flush();
    }

    @Test
    public void weCanNotSendMessageWhichLengthIsMoreThan150Symbols() {
        Assertions.assertFalse(client.lengthCheck("/snd " +
                        "aaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaa"));
    }
}*/
