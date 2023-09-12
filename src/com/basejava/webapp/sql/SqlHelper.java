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
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T executePreparedStatement(String sqlQuery, PreparedStatementExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            LOG.info("Got a connection to the database.");
            return executor.execute(ps);
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                LOG.log(Level.WARNING, "This resume already exists!!!");
                throw new ExistStorageException(null);
            }
            LOG.log(Level.WARNING, "Occurred SqlException");
            throw new StorageException(e);
        }
    }
}