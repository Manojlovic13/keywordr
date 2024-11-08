package com.keywordr.util;

import com.google.gson.*;
import com.keywordr.exception.KeywordrRuntimeException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {
    public static <T> List<T> readObjectsFromJson(String filePath, Class<T> tClass, String jsonArrayKey) {
        Gson gson = new GsonBuilder().create();
        List<T> objects = new ArrayList<>();

        System.out.println("readObjectsFromJson file: " + filePath);

        try (FileReader reader = new FileReader(filePath)) {
            // Parse the JSON file
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonArray jsonArray = jsonElement.getAsJsonObject().getAsJsonArray(jsonArrayKey);

            // Deserialize each JSON object into a Company
            for (JsonElement element : jsonArray) {
                T obj = gson.fromJson(element, tClass);
                objects.add(obj);
            }
        } catch (IOException e) {
            throw new KeywordrRuntimeException(
                    "Failed to read objects of class "
                            + tClass.getName() +
                            " from " +
                            filePath +
                            ", with message: " +
                            e.getMessage()
                    );
        }

        return objects;
    }
}
