package com.basejava.webapp.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SerializationStrategyTest extends AbstractStorageTest{
    protected SerializationStrategyTest() {
        super(new SerializationContext(new ObjectStreamPathStorage(STORAGE_DIR)).getStrategy());
    }

    @Test
    void setStrategy() {
        Storage fileStrategy = new SerializationContext(new ObjectStreamStorage(STORAGE_DIR_FILE)).getStrategy();
        SerializationContext pathStrategy = new SerializationContext(new ObjectStreamPathStorage(STORAGE_DIR));
        Storage actual = pathStrategy.getStrategy();
        assertNotEquals(fileStrategy.getClass(), actual.getClass());

        pathStrategy.setStrategy(new ObjectStreamStorage(STORAGE_DIR_FILE));
        actual = pathStrategy.getStrategy();
        assertEquals(fileStrategy.getClass(), actual.getClass());
    }
}