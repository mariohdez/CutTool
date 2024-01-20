package org.mariocoding.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public class ArgumentParserTest {
    private static final char TAB = '\t';
    private static final char COMMA = ',';
    private static final String FILE_NAME = "fileName";

    @ParameterizedTest
    @MethodSource("fieldListInputsAndResults")
    public void constructorWhenValidFieldListAndFileNameConstructsObject(String argument, List<Integer> expectedFieldList, char expectedDelimiter) {
        ArgumentParser argumentParser = new ArgumentParser(new String[]{argument, FILE_NAME});

        List<Integer> actualFieldList = argumentParser.getFieldList();

        for (int i = 0; i < expectedFieldList.size(); i++) {
            Assertions.assertEquals(expectedFieldList.get(i).intValue(), actualFieldList.get(i).intValue());
        }

        Assertions.assertEquals(expectedDelimiter, argumentParser.getDelimiter().charValue());
        Assertions.assertEquals(FILE_NAME, argumentParser.getFileName());
    }

    @ParameterizedTest
    @MethodSource("fieldListAndDelimiterInputsAndResults")
    public void constructorWithDelimiter(String[] arguments, List<Integer> expectedFieldList, char expectedDelimiter) {
        ArgumentParser argumentParser = new ArgumentParser(arguments);

        List<Integer> actualFieldList = argumentParser.getFieldList();

        Assertions.assertEquals(expectedDelimiter, argumentParser.getDelimiter());

        for (int i = 0; i < expectedFieldList.size(); i++) {
            Assertions.assertEquals(expectedFieldList.get(i).intValue(), actualFieldList.get(i).intValue());
        }

        Assertions.assertEquals(FILE_NAME, argumentParser.getFileName());
    }

    private static Stream<Arguments> fieldListInputsAndResults() {
        return Stream.of(
                Arguments.of("-f1", List.of(1), TAB),
                Arguments.of("-f 1", List.of(1), TAB),
                Arguments.of("-f1,2,3", List.of(1, 2, 3), TAB),
                Arguments.of("-f \"1,2,3\"", List.of(1, 2, 3), TAB),
                Arguments.of("-f 1,2,3", List.of(1, 2, 3), TAB)
        );
    }

    private static Stream<Arguments> fieldListAndDelimiterInputsAndResults() {
        return Stream.of(
                Arguments.of(
                        new String[] {"-f1", "-d,", FILE_NAME}, List.of(1), COMMA),
                Arguments.of(new String[] {"-f1", "-d ,", FILE_NAME}, List.of(1), COMMA),
                Arguments.of(new String[] {"-f1,2,3", "-d \",\"", FILE_NAME}, List.of(1, 2, 3), COMMA),
                Arguments.of(new String[] {"-f1,2,3", "-d\",\"", FILE_NAME}, List.of(1, 2, 3), COMMA),
                Arguments.of(new String[] {"-f1,2,3", "-d\",\"", FILE_NAME}, List.of(1, 2, 3), COMMA)
        );
    }
}
