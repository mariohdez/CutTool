package org.mariocoding.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public class ArgumentParserTest {
    @ParameterizedTest
    @MethodSource("inputsAndResults")
    public void parseFieldListWithValidValues(String argument, List<Integer> expectedFieldList) {
        ArgumentParser argumentParser = new ArgumentParser(new String[] {argument});

        List<Integer> actualFieldList = argumentParser.getFieldList();

        for (int i = 0; i < expectedFieldList.size(); i++) {
            Assertions.assertEquals(expectedFieldList.get(i).intValue(), actualFieldList.get(i).intValue());
        }
    }

    private static Stream<Arguments> inputsAndResults() {
        return Stream.of(
                Arguments.of("-f1", List.of(1)),
                Arguments.of("-f1,2,3", List.of(1,2,3)),
                Arguments.of("-f \"1,2,3\"", List.of(1,2,3)),
                Arguments.of("-f 1,2,3", List.of(1,2,3))
        );
    }
}
