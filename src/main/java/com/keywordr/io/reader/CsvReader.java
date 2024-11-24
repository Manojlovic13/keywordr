package com.keywordr.io.reader;

import com.keywordr.exception.KeywordrRuntimeException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvReader {
    public static HashSet<String> readCsvFile(String filePath) {
        HashSet<String> items;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            items = new HashSet<>(Arrays.asList(line.split(",")));
        } catch (IOException e) {
            throw new KeywordrRuntimeException("Failed to read .csv file + " + filePath + ", with message " + e);
        }

        return items;
    }
}
