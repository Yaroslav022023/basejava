package com.basejava.webapp.sql;

import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
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
                LOG.log(Level.WARNING, "This resume already exists !!!", e);
                throw new ExistStorageException(null);
            }
            LOG.log(Level.WARNING, "Occurred SqlException", e);
            throw new StorageException(e);
        }
    }

    public <T> T transactionalExecute(String sqlQuery, PreparedStatementExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T result = executePreparedStatement(sqlQuery, executor);
                conn.commit();
                return result;
            } catch (SQLException e) {
                LOG.log(Level.WARNING, "The transaction has been canceled. Invoking 'conn.rollback()'", e);
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Occurred SqlException", e);
            throw new StorageException(e);
        }
    }

    public <K, V> void iterateAndBatchExecute(Map<K, V> data, PreparedStatement ps, MapEntryConsumer<K, V> executor) {
        try {
            for (Map.Entry<K, V> entry : data.entrySet()) {
                executor.execute(entry);
                ps.addBatch();
            }
            LOG.info("Completed all addBatch() in loop for()");
            ps.executeBatch();
            LOG.info("Completed executeBatch()");
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Occurred SqlException", e);
            throw new StorageException(e);
        }
    }
}