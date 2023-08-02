package com.basejava.webapp.model;

import java.io.Serial;
import java.util.Objects;

public class TextSection extends Section{
    @Serial
    private static final long serialVersionUID = 1L;
    private String text;

    public TextSection(String text) {
        Objects.requireNonNull(text, "text must be not null");
        this.text = text;
    }

    public final String getText() {
        return text;
    }

    public final void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextSection that = (TextSection) o;
        return getText().equals(that.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText());
    }

    @Override
    public String toString() {
        return text;
    }
}