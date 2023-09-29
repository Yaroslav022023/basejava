package com.basejava.webapp.sql;

import com.basejava.webapp.model.Resume;

import java.sql.SQLException;

@FunctionalInterface
public interface RunnableWithSqlException {
    void execute(Resume resume) throws SQLException;
}