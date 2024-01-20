package org.mariocoding.utilities;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {
    private boolean hasFieldList;
    private List<Integer> fieldList = new ArrayList<>();

    private static final String FIELDS_PREFIX = "-f";
    private static final String QUOTE = "\"";

    public ArgumentParser(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("Arguments should not be null.");
        }

        if (args.length == 0) {
            throw new IllegalArgumentException("Arguments should not be empty.");
        }

        int i = 0;

        while (i < args.length) {
            if (args[i].startsWith(this.FIELDS_PREFIX)) {
                this.hasFieldList = true;
                String unparsedFieldList;

                if (args[i].length() > 2) {
                    unparsedFieldList = args[i].substring(2);
                } else if (i < args.length) {
                    unparsedFieldList = args[i+1];
                    i++;
                } else {
                    throw new IllegalArgumentException("Arguments were malformed.");
                }

                this.parseFieldList(unparsedFieldList);
            }

            i++;
        }
    }

    public List<Integer> getFieldList() {
        return this.fieldList;
    }

    private void parseFieldList(String unparsedFieldList) {
        String errorMessage = String.format("Field list is malformed: %1$s.", unparsedFieldList);
        String trimmedUnparsedFieldList = unparsedFieldList.trim();
        int len = trimmedUnparsedFieldList.length();
        boolean validNoQuotes = len > 0 && Character.isDigit(trimmedUnparsedFieldList.charAt(0)) && Character.isDigit(trimmedUnparsedFieldList.charAt(len - 1));
        boolean validWithQuotes = len > 0 && trimmedUnparsedFieldList.startsWith(this.QUOTE) && trimmedUnparsedFieldList.endsWith(this.QUOTE);

        if (!validWithQuotes && !validNoQuotes) {
            throw new IllegalArgumentException(errorMessage);
        }

        String noQuotesFieldList;

        if (validWithQuotes) {
            noQuotesFieldList = trimmedUnparsedFieldList.substring(1, len - 1);
        } else {
            noQuotesFieldList = trimmedUnparsedFieldList;
        }

        for (int i = 0; i < noQuotesFieldList.length(); i++) {
            if (Character.isDigit(noQuotesFieldList.charAt(i)) || noQuotesFieldList.charAt(i) == ',') {
                continue;
            }

            throw new IllegalArgumentException(errorMessage);
        }

        String[] elements = noQuotesFieldList.split(",");

        for (int i = 0; i < elements.length; i++) {
            try {
                this.fieldList.add(Integer.parseInt(elements[i]));
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException(errorMessage, nfe);
            }
        }
    }
}
