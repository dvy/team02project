package com.db.edu.query;

import com.db.edu.exceptions.QueryProcessingException;

import java.util.Objects;

public class QueryFactory {

    private QueryFactory() {
    }

    public static Query getQuery(String query) {
        ParsedQuery parsed = QueryFactory.parse(query);
        switch (parsed.getType()) {
            case "/snd":
                return new SendQuery(parsed.getBody());
            case "/hist":
                return new HistoryQuery(parsed.getBody());
            case "/chid":
                return new ChangeIDQuery(parsed.getBody());
            case "/exit":
                return new ExitQuery(parsed.getBody());

            default:
                throw new QueryProcessingException("Unknown command");
        }
    }

    private static ParsedQuery parse(String string) {
        if (Objects.equals(string, "")) throw new QueryProcessingException("Empty query");
        if (!string.startsWith("/")) throw new QueryProcessingException("Wrong query format. Command starts with '/'");

        String[] parsed = string.split("\\s", 2);

        if (parsed.length == 1) return new ParsedQuery(parsed[0], null);
        else return new ParsedQuery(parsed[0], parsed[1]);
    }

    private static class ParsedQuery {
        private final String type;
        private final String body;

        ParsedQuery(String typeString, String body) {
            this.type = typeString;
            this.body = body;
        }

        public String getType() {
            return type;
        }

        public String getBody() {
            return body;
        }
    }
}
