package com.basejava.webapp.storage;

public class SerializationContext {
    private SerializationStrategy strategy;

    public SerializationContext(SerializationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SerializationStrategy strategy) {
        this.strategy = strategy;
    }

    public Storage getStrategy() {
        return (Storage) strategy;
    }
}