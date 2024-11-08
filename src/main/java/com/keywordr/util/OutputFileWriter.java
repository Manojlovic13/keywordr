package com.keywordr.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class OutputFileWriter {
    public static void writeToJson(Set<Job> jobSet, String filePath, String jsonArrayKey, String outputMode) {
        if (StringUtils.isBlank(filePath)) {
            try {
                filePath = createWriteFile();
            } catch (IOException e) {
                throw new KeywordrRuntimeException("Failed to create output file, with message: " + e.getMessage());
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject rootObject = new JsonObject();
        JsonArray jsonArray;

        // If mode is set to append, read all jobs in the file & mix with new jobs
        if (outputMode.equalsIgnoreCase("append")) {
            List<Job> jobs = JsonReader.readObjectsFromJson(filePath, Job.class,"jobs");
            jobSet.addAll(jobs);
        }

        // Write to the file, new instance of FileWriter deletes all content from source file
        try (FileWriter writer = new FileWriter(filePath)) {
            jsonArray = gson.toJsonTree(jobSet).getAsJsonArray();
            rootObject.add(jsonArrayKey, jsonArray);
            gson.toJson(rootObject, writer);
        } catch (IOException e) {
            throw new KeywordrRuntimeException("Failed to write jobs to " + filePath + ", with message: " + e.getMessage());
        }
    }

    private static String createWriteFile() throws IOException {
        // Generate unique file name with date-time suffix
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String directory = "../result";
        String file = directory + "/keywordr-" + timestamp + ".json";

        // Ensure that directory exists
        Files.createDirectories(Path.of(directory));

        // Create file
        Files.createFile(Path.of(file));
        return file;
    }
}
