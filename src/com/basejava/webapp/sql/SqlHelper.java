package com.basejava.webapp.sql;

import com.basejava.webapp.exceptions.StorageException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqlHelper {
    private static final Logger LOG = Logger.getLogger(SqlHelper.class.getName());

    public static void executeWithException(ConnectionFactory connectionFactory, SqlExecutor executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            executor.execute(conn);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "An SQL exception occurred", e);
            throw new StorageException(e);
        }
    }

    public static <T> T executeWithException(ConnectionFactory connectionFactory, SqlExecutorWithReturn<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            return executor.execute(conn);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "An SQL exception occurred", e);
            throw new StorageException(e);
        }
    }
}