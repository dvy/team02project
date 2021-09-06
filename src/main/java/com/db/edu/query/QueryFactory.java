package com.db.edu.query;

import com.db.edu.exceptions.QueryProcessingException;
import java.util.Objects;


public class QueryFactory {
    public static Query GetQuery(String query) {
        ParsedQuery parsed = QueryFactory.parse(query);
        switch (parsed.type) {
            case SEND:
                if(parsed.body == null) throw new QueryProcessingException("Too few arguments: /snd command get non-empty string");
                if(parsed.body.length() > 150) throw new QueryProcessingException("Too long message. Message length should be shorter");
                return new SendQuery(parsed.body);
            case HISTORY:
                if(parsed.body != null) throw new QueryProcessingException("Too many arguments: /hist command does not get arguments");
                return new HistoryQuery();
            default:
                throw new QueryProcessingException("Unknown command");
        }
    }

    private static ParsedQuery parse(String string) {
        if(Objects.equals(string, "")) throw new QueryProcessingException("Empty message");
        if(!string.startsWith("/")) throw new QueryProcessingException("Wrong query format");

        String[] parsed = string.split("\\s");

        if(parsed.length == 1) return new ParsedQuery(parsed[0], null);
        else return new ParsedQuery(parsed[0], parsed[1]);
    }

    private static class ParsedQuery {
        public QueryType type;
        public String body;
        ParsedQuery(String typeString, String body) {
            this.type = QueryType.valueOf(typeString);
            this.body = body;
        }
    }
}
