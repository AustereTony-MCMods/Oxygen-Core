package austeretony.oxygen_core.common.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonUtils {

    public static JsonElement getInternalJsonData(String path) throws IOException {
        JsonElement rawData;
        try (
                InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(path);
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            rawData = new JsonParser().parse(reader);
        }
        return rawData;
    }

    public static JsonElement getExternalJsonData(String path) throws IOException {
        JsonElement rawData;
        try (
                InputStream inputStream = new FileInputStream(new File(path));
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            rawData = new JsonParser().parse(reader);
        }
        return rawData;
    }

    public static void createExternalJsonFile(String path, JsonElement data) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8)) {
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(data, writer);
        }
    }
}
