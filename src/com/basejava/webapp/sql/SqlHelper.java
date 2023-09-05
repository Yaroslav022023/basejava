package com.basejava.webapp.sql;

import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqlHelper {
    private static final Logger LOG = Logger.getLogger(SqlHelper.class.getName());
    private final Connection connection;

    public SqlHelper(ConnectionFactory connectionFactory) {
        try {
            connection = connectionFactory.getConnection();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Database connection error", e);
            throw new StorageException(e);
        }
    }

    public <T> T executePreparedStatement(String sqlQuery, PreparedStatementExecutor<T> executor) {
        return executeWithException(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
                return executor.execute(ps);
            }
        });
    }

    public <T> T executeWithException(SqlExecutor<T> executor) {
        try {
            return executor.execute(connection);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "An SQL exception occurred", e);
            throw new StorageException(e);
        }
    }

    public void checkExistingResume(SQLException e, String uuid) {
        if ("23505".equals(e.getSQLState())) {
            LOG.log(Level.WARNING, "Resume '" + uuid +
                    "' already exist");
            throw new ExistStorageException(uuid);
        }
        LOG.log(Level.SEVERE, "An SQL exception occurred", e);
        throw new StorageException(e);
    }
}