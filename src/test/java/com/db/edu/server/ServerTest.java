package com.db.edu.server;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {
    @Test
    public void shouldThrowExceptionWhenTheWrongPortSelected() {
        Server server = new Server(-1);
        assertThrows(IllegalArgumentException.class, () -> server.start());
    }
}
