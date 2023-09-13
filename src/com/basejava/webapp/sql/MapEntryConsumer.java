package com.basejava.webapp.sql;

import java.sql.SQLException;
import java.util.Map;

@FunctionalInterface
public interface MapEntryConsumer<K, V> {
    void execute(Map.Entry<K, V> entry) throws SQLException;
}