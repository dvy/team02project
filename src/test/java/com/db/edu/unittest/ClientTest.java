package com.db.edu.unittest;

import com.db.edu.Client;
import com.db.edu.Server;
import org.junit.jupiter.api.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class ClientTest {

    private Client client = new Client();
    private Server serverMock = mock(Server.class);
    private DataOutputStream output = mock(DataOutputStream.class);
    private DataInputStream input = mock(DataInputStream.class);

    @Test
    public void weCanSendStringMessageToServer() throws IOException {
        client.sendMessage("Hello", output);

        verify(output, times(1)).writeUTF("Hello");
        verify(output, times(1)).flush();
    }

    /*@Test
    public void weCanReadStringMessageFromServer() throws IOException {
        when(input.readUTF()).thenReturn("Hello");

        client.readAnswer(input);

        verify(input,times(1)).readUTF();
    }*/
}
