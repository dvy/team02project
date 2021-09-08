package com.db.edu;

import com.db.edu.query.HistoryQuery;
import com.db.edu.query.SendQuery;
import com.db.edu.utils.History;
import com.db.edu.utils.MessageProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

import static com.db.edu.utils.MessageProcessor.*;
import static org.mockito.Mockito.*;

public class MessageProcessorTest {
    @Test
    public void historyLoadExecutesWhenprocessMessageHistoryQueryExecuted(){
        HistoryQuery historyQueryMock = mock(HistoryQuery.class);
        History historyMock = mock(History.class);

        setHistory(historyMock);
        processMessage(historyQueryMock);
        verify(historyMock, times(1)).load();
    }
}
