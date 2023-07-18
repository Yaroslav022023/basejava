package com.basejava.webapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section{
    private List<String> texts = new ArrayList<>();

    public ListSection(String... texts) {
        this.texts = Arrays.asList(texts);
    }

    public List<String> getTexts() {
        return texts;
    }

    public void addTexts(String text) {
        texts.add(text);
    }

    public void removeTexts(String text) {
        texts.remove(text);
    }

    public void clear() {
        texts.clear();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTexts());
    }

    @Override
    public String toString() {
        return texts.toString();
    }
}