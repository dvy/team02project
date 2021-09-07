package com.db.edu.unittest.query;

import com.db.edu.exceptions.QueryProcessingException;
import com.db.edu.query.QueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeIDTest {
    @Test
    void shouldReturnCorrectQueryWhenTryChangeIDtoCorrect() {
        assertEquals("/chid nickname", QueryFactory.GetQuery("/chid nickname").toString());
    }

    @Test
    void shouldReturnErrorWhenTryChangeIDtoEmpty() {
        assertThrows(QueryProcessingException.class, () -> QueryFactory.GetQuery("/chid"));
    }
}
