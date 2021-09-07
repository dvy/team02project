package com.db.edu.query;

import com.db.edu.exceptions.QueryProcessingException;
import java.util.Objects;


public class QueryFactory {
    public static Query GetQuery(String query) {
        ParsedQuery parsed = QueryFactory.parse(query);
        switch (parsed.type) {
            case "/snd":
                return new SendQuery(parsed.body);
            case "/hist":
                return new HistoryQuery(parsed.body);
            default:
                throw new QueryProcessingException("Unknown command");
        }
    }

    private static ParsedQuery parse(String string) {
        if(Objects.equals(string, "")) throw new QueryProcessingException("Empty query");
        if(!string.startsWith("/")) throw new QueryProcessingException("Wrong query format. Command starts with '/'");

        String[] parsed = string.split("\\s");

        if(parsed.length == 1) return new ParsedQuery(parsed[0], null);
        else return new ParsedQuery(parsed[0], parsed[1]);
    }

    private static class ParsedQuery {
        public String type;
        public String body;
        ParsedQuery(String typeString, String body) {
            this.type = typeString;
            this.body = body;
        }
    }
}
