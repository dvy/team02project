package com.db.edu.unittest.query;

import com.db.edu.exceptions.QueryProcessingException;
import com.db.edu.query.SendQuery;
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
        assertEquals("/snd Hello", new SendQuery("Hello").toString());
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
