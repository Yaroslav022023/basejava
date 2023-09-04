package com.basejava.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlExecutor<T> {
    T execute(Connection conn) throws SQLException;
}