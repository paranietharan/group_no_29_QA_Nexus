package com.qanexus.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public final class TestDataLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestDataLoader() {
    }

    public static Map<String, String> loadJsonMap(String resourcePath) {
        try (InputStream input = TestDataLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalArgumentException("Missing test data resource: " + resourcePath);
            }
            return MAPPER.readValue(input, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read test data: " + resourcePath, ex);
        }
    }
}
