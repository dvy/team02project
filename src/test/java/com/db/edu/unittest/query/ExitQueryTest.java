package com.db.edu.unittest.query;

import com.db.edu.exceptions.EndOfSessionException;
import com.db.edu.exceptions.QueryProcessingException;
import com.db.edu.query.QueryFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExitQueryTest {

    @Test
    void shouldReturnCorrectQueryWhenTryExitCorrect() {
        assertThrows(EndOfSessionException.class, () -> QueryFactory.getQuery("/exit"));
    }

    @Test
    void shouldReturnErrorWhenTryChangeIDtoEmpty() {
        assertThrows(QueryProcessingException.class, () -> QueryFactory.getQuery("/exit qwerty"));
    }
}
