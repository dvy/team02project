package com.db.edu.unittest;

import com.db.edu.SysoutCaptureAndAssertionAbility;
import com.db.edu.client.Client;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

public class ClientTest implements SysoutCaptureAndAssertionAbility {
    private final Client client = new Client("localhost", 10_000);
    private final DataOutputStream output = mock(DataOutputStream.class);
    private final DataInputStream input = mock(DataInputStream.class);

    @BeforeEach
    public void setUpSystemOut() {
        resetOut();
        captureSysout();
    }

    @AfterEach
    public void tearDown() {
        resetOut();
    }

    @Test
    public void weCanMakeAConnectionWithServer() {
        ExecutorService thread1 = Executors.newSingleThreadExecutor();
        ExecutorService thread2 = Executors.newSingleThreadExecutor();

        thread1.execute(() -> {
            try (final ServerSocket listener = new ServerSocket(10_002);
                 final Socket connection = listener.accept();
                 final DataOutputStream output = new DataOutputStream(
                         new BufferedOutputStream(connection.getOutputStream()))) {

                output.writeUTF("Hello");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread2.execute(() -> {
            resetOut();
            captureSysout();

            Client client = new Client("localhost", 10_002);
            client.socketConnectionRun();

            assertSysoutContains("Hello");

            resetOut();
        });

        thread2.shutdown();
        thread1.shutdown();
    }
}
