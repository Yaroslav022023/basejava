package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MapStorageTest extends AbstractStorageTest{
    protected MapStorageTest() {
        super(new MapStorage());
    }

    @Override
    @Test
    final void getAll() {
        Map<String, Resume> expected = new HashMap<>();
        expected.put(r1.getUuid(), r1);
        expected.put(r2.getUuid(), r2);
        expected.put(r3.getUuid(), r3);

        Assertions.assertArrayEquals(expected.values().toArray(new Resume[0]), storage.getAll());
    }
}