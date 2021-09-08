package com.db.edu.query;

import com.db.edu.exceptions.QueryProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SendQueryTest {

    @Test
    void shouldThrowQueryProcessingExceptionIfMessageIsNull() {
        assertThrows(QueryProcessingException.class,
                () -> new SendQuery(null));
    }

    @Test
    void shouldDecorateQueryWithPrefix() {
        assertTrue(new SendQuery("Hello", "122.122.2.2").toString().contains("122.122.2.2 : Hello"));
    }

    @Test
    void shouldThrowQueryProcessingExceptionIfMessageIsTooLong() {
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
