package com.db.edu.unittest;

import com.db.edu.exceptions.QueryProcessingException;
import com.db.edu.query.QueryFactory;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class QueryTest {
    @Test
    void shouldReturnErrorWhenTrySendEmptyMessage() {
        assertThrows(QueryProcessingException.class, () -> QueryFactory.GetQuery("/snd"));
    }

    @Test
    void shouldReturnCorrectQueryWhenTrySendCorrectMessage() {
        assertEquals("/snd hello", QueryFactory.GetQuery("/snd hello").toString());
    }

    @Test
    void shouldReturnErrorWhenTrySendWrongHistoryQuery() {
        assertThrows(QueryProcessingException.class, () -> QueryFactory.GetQuery("/hist hello"));
    }

    @Test
    void shouldReturnCorrectQueryWhenTrySendCorrectHistoryQuery() {
        assertEquals("/hist", QueryFactory.GetQuery("/hist").toString());
    }

    @Test
    void shouldReturnErrorWhenTrySendUnknownTypeOfQuery() {
        assertThrows(QueryProcessingException.class, () -> QueryFactory.GetQuery("/hello"));
    }

    @Test
    void shouldReturnErrorWhenTrySendTooLongMessage() {
        assertThrows(QueryProcessingException.class,
                () -> QueryFactory.GetQuery("/snd " +
                        "longmessagelongmessagelongmessagelongmessagelongmessagelongmessagelongmessage" +
                        "longmessagelongmessagelongmessagelongmessagelongmessagelongmessagelongmessage"));
    }
}
