package com.basejava.webapp.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface RunnableWithSqlException {
    void execute() throws SQLException;
}