package com.basejava.webapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section{
    private final List<String> texts;

    public ListSection(String... texts) {
        if (texts.length == 0) {
            throw new NullPointerException("ListSection must be not null");
        }
        this.texts = new ArrayList<>(Arrays.asList(texts));
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
        return texts.toString();
    }
}