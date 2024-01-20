package org.mariocoding.utilities;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {
    private boolean hasFileName;
    private char delimiter = '\t';
    private final List<Integer> fieldList = new ArrayList<>();
    private final List<String> fileNames = new ArrayList<>();

    private static final String FIELDS_PREFIX = "-f";
    private static final String QUOTE = "\"";
    private static final String DELIMITER_PREFIX = "-d";
    private static final String ERROR_MESSAGE_FORMATTER = "Field list is malformed: %1$s.";
    private static final String ERROR_MESSAGE_DELIMITER_FORMATTER = "Delimiter is malformed: %1s.";

    public ArgumentParser(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("Arguments should not be null.");
        }

        if (args.length == 0) {
            throw new IllegalArgumentException("Arguments should not be empty.");
        }

        int i = 0;

        while (i < args.length) {
            if (args[i].startsWith(FIELDS_PREFIX) && !this.hasFileName) {
                String unparsedFieldList;

                if (args[i].length() > 2) {
                    unparsedFieldList = args[i].substring(2);
                } else if ((i + 1) < args.length) {
                    unparsedFieldList = args[i + 1];
                    i++;
                } else {
                    throw new IllegalArgumentException("Arguments were malformed.");
                }

                unparsedFieldList = cleanArgumentModifier(unparsedFieldList);
                validateFieldList(unparsedFieldList);

                this.parseFieldList(unparsedFieldList);
            } else if (args[i].startsWith(DELIMITER_PREFIX) && !this.hasFileName) {
                String unparsedDelimter;
                if (args[i].length() > 2) {
                    unparsedDelimter = args[i].substring(2);
                } else if ((i + 1) < args.length) {
                    unparsedDelimter = args[i + 1];
                    i++;
                } else {
                    throw new IllegalArgumentException("Delimiter argument modifier malformed.");
                }

                unparsedDelimter = cleanArgumentModifier(unparsedDelimter);
                validateDelimiter(unparsedDelimter);

                this.delimiter = unparsedDelimter.charAt(0);
            } else {
                this.hasFileName = true;
                this.fileNames.add(args[i]);
            }

            i++;
        }
    }

    public List<Integer> getFieldList() {
        return this.fieldList;
    }

    public String getFileName() {
        return this.fileNames.size() > 0 ? this.fileNames.get(0) : null;
    }

    public Character getDelimiter() {
        return this.delimiter;
    }

    private void validateFieldList(String unparsedFieldList) {
        String errorMessage = String.format(ERROR_MESSAGE_FORMATTER, unparsedFieldList);
        int len = unparsedFieldList.length();
        boolean validNoQuotes = len > 0 && Character.isDigit(unparsedFieldList.charAt(0)) && Character.isDigit(unparsedFieldList.charAt(len - 1));
        boolean validWithQuotes = len > 0 && unparsedFieldList.startsWith(QUOTE) && unparsedFieldList.endsWith(QUOTE);

        if (!validWithQuotes && !validNoQuotes) {
            throw new IllegalArgumentException(errorMessage);
        }

        for (int i = 0; i < unparsedFieldList.length(); i++) {
            if (Character.isDigit(unparsedFieldList.charAt(i)) || unparsedFieldList.charAt(i) == ',') {
                continue;
            }

            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateDelimiter(String delimiter) {
        if (delimiter.length() > 1) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_DELIMITER_FORMATTER, delimiter));
        }
    }

    private void parseFieldList(String unparsedFieldList) {
        String[] elements = unparsedFieldList.split(",");

        for (String element : elements) {
            try {
                this.fieldList.add(Integer.parseInt(element));
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException(String.format(ERROR_MESSAGE_FORMATTER, unparsedFieldList), nfe);
            }
        }
    }

    private String cleanArgumentModifier(String unparsedArgumentModifier) {
        String trimmedUnparsedArgumentModifier = unparsedArgumentModifier.trim();
        int len = trimmedUnparsedArgumentModifier.length();
        String noQuotesArgumentModifier;

        if (len > 0 && trimmedUnparsedArgumentModifier.startsWith(QUOTE) && trimmedUnparsedArgumentModifier.endsWith(QUOTE)) {
            noQuotesArgumentModifier = trimmedUnparsedArgumentModifier.substring(1, len - 1);
        } else {
            noQuotesArgumentModifier = trimmedUnparsedArgumentModifier;
        }

        return noQuotesArgumentModifier;
    }
}
