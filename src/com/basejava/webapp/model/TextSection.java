package com.basejava.webapp.model;

import java.util.Objects;

public class TextSection extends Section{
    private String text;

    public TextSection(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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