package com.db.edu.client;

import com.db.edu.SysoutCaptureAndAssertionAbility;
import com.db.edu.exceptions.QueryProcessingException;
import org.junit.jupiter.api.*;
import java.io.*;
import static org.mockito.Mockito.*;

public class ClientTest implements SysoutCaptureAndAssertionAbility {
    private final Client client = new Client("localhost", 10_000);
    private final DataOutputStream outputMock = mock(DataOutputStream.class);

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
    public void shouldThrowExceptionWhenWeTryToProcessIncorrectMessage() throws QueryProcessingException {
        Assertions.assertThrows(QueryProcessingException.class, () -> {
            client.processQuery("Hello", outputMock);
        });
    }
}
