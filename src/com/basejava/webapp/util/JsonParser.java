package com.basejava.webapp.util;

import com.basejava.webapp.model.Section;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;

public class JsonParser {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Section.class, new JsonSectionAdapter<Section>())
            .registerTypeAdapter(LocalDate.class, new JsonSectionAdapter.LocalDateAdapter())
            .create();

    public static <T> T read(Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }

    public static <T> void write(T object, Writer writer) {
        GSON.toJson(object, writer);
    }

    public static <T> T read(String content, Type type) {
        return GSON.fromJson(content, type);
    }

    public static <T> String write(T object) {
        return GSON.toJson(object);
    }
}