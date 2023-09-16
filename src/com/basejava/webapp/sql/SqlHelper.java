package com.basejava.webapp.sql;

import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public <T> T executePreparedStatement(Connection conn, String sqlQuery, PreparedStatementExecutor<T> executor) {
        try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            LOG.info("Got a connection to the database.");
            return executor.execute(ps);
        } catch (SQLException e) {
            handleSQLException(e);
            throw new StorageException(e);
        }
    }

    public <T> T transactionalExecute(ConnectionExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T result = executor.execute(conn);
                conn.commit();
                LOG.info("Completed: conn.commit())");
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

    public void addContacts(ResultSet rs, Resume resume) {
        try {
            String value = rs.getString("value");
            if (value != null) {
                resume.addContact(ContactType.valueOf(rs.getString("type")), value);
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQLException occurred while trying to get 'value' from the database", e);
            throw new StorageException(e);
        }
    }

    private void handleSQLException(SQLException e) {
        if ("23505".equals(e.getSQLState())) {
            LOG.log(Level.WARNING, "This resume already exists !!!", e);
            throw new ExistStorageException(null);
        }
        LOG.log(Level.WARNING, "Occurred SqlException", e);
    }
}