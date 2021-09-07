package com.db.edu.query;

import com.db.edu.exceptions.EndOfSessionException;
import com.db.edu.exceptions.QueryProcessingException;

public class ExitQuery implements Query {
    public ExitQuery(String message) {
        if (message != null) {
            throw new QueryProcessingException("Too many arguments: /exit command does not get arguments");
        } else {
            throw new EndOfSessionException("Session will be closed now");
        }
    }

    @Override
    public String toString() {
        return "/exit";
    }
}
