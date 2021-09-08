package com.db.edu.query;

import com.db.edu.exceptions.QueryProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SendQueryTest {

    @Test
    public void shouldThrowQueryProcessingExceptionIfMessageIsNull() {
        assertThrows(QueryProcessingException.class,
                () -> new SendQuery(null));
    }

    @Test
    public void shouldDecorateQueryWithPrefix() {
        assertTrue(new SendQuery("Hello", "122.122.2.2").toString().contains("122.122.2.2 : Hello"));
    }

    @Test
    public void shouldThrowQueryProcessingExceptionIfMessageIsTooLong() {
        assertThrows(QueryProcessingException.class,
                () -> new SendQuery("aaaaaaaaaaaaaaaaaaaa" +
                                    "aaaaaaaaaaaaaaaaaaaa" +
                                    "aaaaaaaaaaaaaaaaaaaa" +
                                    "aaaaaaaaaaaaaaaaaaaa" +
                                    "aaaaaaaaaaaaaaaaaaaa" +
                                    "aaaaaaaaaaaaaaaaaaaa" +
                                    "aaaaaaaaaaaaaaaaaaaa" +
                                    "aaaaaaaaaaaaaaaaaaaa"));
    }
}
