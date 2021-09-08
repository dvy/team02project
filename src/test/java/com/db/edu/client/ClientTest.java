package com.db.edu.client;

import com.db.edu.SysoutCaptureAndAssertionAbility;
import com.db.edu.exceptions.QueryProcessingException;
import com.db.edu.utils.NetworkIOController;
import org.junit.jupiter.api.*;
import java.io.*;
import static org.mockito.Mockito.*;

public class ClientTest implements SysoutCaptureAndAssertionAbility {

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
    public void shouldThrowExceptionWhenWeTryToProcessIncorrectMessage() throws QueryProcessingException, IOException {
       Client client = new Client(mock(NetworkIOController.class));
    }
}
