package com.db.edu.query;

import com.db.edu.exceptions.EndOfSessionException;
import com.db.edu.exceptions.QueryProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryFactoryTest {
    @Test
    public void weWillReceiveQueryProcessingExceptionIfWeWillParseEmptyString() {
        assertThrows(QueryProcessingException.class,
                () -> QueryFactory.getQuery(""));
    }

    @Test
    public void weWillReceiveQueryProcessingExceptionIfWeWillParseSlashString() {
        assertThrows(QueryProcessingException.class,
                () -> QueryFactory.getQuery("hi"));
    }

    @Test
    public void weWillReceiveQueryProcessingExceptionIfWeWillEnterIncorrectCommand() {
        assertThrows(QueryProcessingException.class,
                () -> QueryFactory.getQuery("/incorrect command"));
    }

    @Test
    void shouldReturnErrorWhenTrySendEmptyMessage() {
        assertThrows(QueryProcessingException.class, () -> QueryFactory.getQuery("/snd"));
    }

    @Test
    void shouldReturnCorrectQueryWhenTrySendCorrectMessage() {
        assertEquals("/snd hello", QueryFactory.getQuery("/snd hello").toString());
    }

    @Test
    void shouldReturnErrorWhenTrySendWrongHistoryQuery() {
        assertThrows(QueryProcessingException.class, () -> QueryFactory.getQuery("/hist hello"));
    }

    @Test
    void shouldReturnCorrectQueryWhenTrySendCorrectHistoryQuery() {
        assertEquals("/hist", QueryFactory.getQuery("/hist").toString());
    }

    @Test
    void shouldThrowEndOfSessionExceptionWhenTrySendCorrectExitQuery() {
        assertThrows(EndOfSessionException.class, () -> {
            QueryFactory.GetQuery("/exit");
        });
    }

    @Test
    void shouldThrowQueryProcessingExceptionWhenTrySendIncorrectExitQuery() {
        assertThrows(QueryProcessingException.class, () -> {
            QueryFactory.GetQuery("/exit hello");
        });
    }

    @Test
    void shouldReturnErrorWhenTrySendUnknownTypeOfQuery() {
        assertThrows(QueryProcessingException.class, () -> QueryFactory.getQuery("/hello"));
    }

    @Test
    void shouldReturnErrorWhenTrySendTooLongMessage() {
        assertThrows(QueryProcessingException.class,
                () -> QueryFactory.getQuery("/snd " +
                        "longmessagelongmessagelongmessagelongmessagelongmessagelongmessagelongmessage" +
                        "longmessagelongmessagelongmessagelongmessagelongmessagelongmessagelongmessage"));
    }
}
