package com.db.edu.unittest.query;

import com.db.edu.exceptions.EndOfSessionException;
import com.db.edu.exceptions.QueryProcessingException;
import com.db.edu.query.QueryFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryFactoryTest {
    @Test
    public void weWillReceiveQueryProcessingExceptionIfWeWillParseEmptyString() {
        assertThrows(QueryProcessingException.class,
                () -> QueryFactory.GetQuery(""));
    }

    @Test
    public void weWillReceiveQueryProcessingExceptionIfWeWillParseSlashString() {
        assertThrows(QueryProcessingException.class,
                () -> QueryFactory.GetQuery("hi"));
    }

    @Test
    public void weWillReceiveQueryProcessingExceptionIfWeWillEnterIncorrectCommand() {
        assertThrows(QueryProcessingException.class,
                () -> QueryFactory.GetQuery("/incorrect command"));
    }

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
