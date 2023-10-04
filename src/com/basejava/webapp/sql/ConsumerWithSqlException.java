package com.basejava.webapp.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface ConsumerWithSqlException<T> {
    void execute(T value) throws SQLException;
}