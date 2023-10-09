package com.basejava.webapp.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section{
    @Serial
    private static final long serialVersionUID = 1L;
    private List<String> texts;

    public ListSection() {}

    public ListSection(String... texts) {
        Objects.requireNonNull(texts, "text must be not null");
        final List<String> edited = new ArrayList<>(Arrays.asList(texts));
        edited.removeIf(string -> string.trim().isEmpty());
        this.texts = edited;
    }

    public ListSection(List<String> texts) {
        this.texts = texts;
    }

    public final List<String> getTexts() {
        return texts;
    }

    public final void addText(String text) {
        texts.add(text);
    }

    public final void removeText(String text) {
        texts.remove(text);
    }

    public final void clear() {
        texts.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return getTexts().equals(that.getTexts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTexts());
    }

    @Override
    public String toString() {
        return String.join("\n", texts);
    }
}