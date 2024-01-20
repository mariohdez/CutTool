package org.mariocoding;

import org.mariocoding.utilities.ArgumentParser;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Cut {
    public static void main(String[] args) {
        try {
            new Cut().runApplication(args);
        } catch (Exception e) {
            System.out.println("exception occurred." + e);
        }
    }

    public void runApplication(String[] args) throws IOException, URISyntaxException {
        ArgumentParser argumentParser = new ArgumentParser(args);
        String fileName = argumentParser.getFileName();

        InputStream inputStream;

        if (fileName == null) {
            inputStream = System.in;
        } else {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            inputStream = classloader.getResourceAsStream(fileName);
        }

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                String[] elements = line.split("\t");
                int fieldCount = elements.length;
                List<Integer> fieldList = argumentParser.getFieldList();

                for (int i = 0; i < fieldList.size(); i++) {
                    int fieldNumber = fieldList.get(i).intValue() - 1;
                    if (fieldNumber >= fieldCount) {
                        continue;
                    }
                    System.out.print(elements[fieldNumber]);
                }

                System.out.println();
            }
        }
    }

}