package com.basejava.webapp.sql;

import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.*;

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

    public final <T> T provideConnection(String sqlQuery, PreparedStatementExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error during provideConnection()", e);
            throw new StorageException(e);
        }
    }

    public final <T> void transactionalExecute(ConnectionExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                executor.execute(conn);
                conn.commit();
                LOG.info("Completed: conn.commit())");
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

    public final <T> T executePreparedStatement(Connection conn, String sqlQuery,
                                                PreparedStatementExecutor<T> executor) {
        try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            LOG.info("Got a connection to the database.");
            return executor.execute(ps);
        } catch (SQLException e) {
            handleSQLException(e);
            throw new StorageException(e);
        }
    }

    public final void iterateContactsAndBatchExecute(Resume resume, PreparedStatement ps) {
        LOG.info("iterateContactsAndBatchExecute()...");
        handlePreparedStatement(ps, "CONTACTS",
                () -> {
                    for (Map.Entry<ContactType, String> entry : resume.getAllContacts().entrySet()) {
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, entry.getKey().name());
                        ps.setString(3, entry.getValue());
                        ps.addBatch();
                    }
                });
        LOG.info("iterateContactsAndBatchExecute(): Finish!");
    }

    public final void iterateSectionsAndBatchExecute(Resume resume, PreparedStatement ps) {
        LOG.info("iterateSectionsAndBatchExecute()...");
        handlePreparedStatement(ps, "SECTIONS",
                () -> {
                    for (Map.Entry<SectionType, Section> entry : resume.getAllSections().entrySet()) {
                        SectionType sectionType = entry.getKey();
                        switch (sectionType) {
                            case OBJECTIVE, PERSONAL -> {
                                ps.setString(1, sectionType.name());
                                ps.setString(2,
                                        ((TextSection) resume.getSection(sectionType)).getText());
                            }
                            case ACHIEVEMENT, QUALIFICATION -> {
                                ps.setString(1, sectionType.name());
                                ps.setString(2, String.join("\n",
                                        ((ListSection) resume.getSection(sectionType)).getTexts()));
                            }
                        }
                        ps.setString(3, resume.getUuid());
                        ps.addBatch();
                    }
                });
        LOG.info("iterateSectionsAndBatchExecute(): Finish!");
    }

    public final void addContactToResume(ResultSet rs, Resume resume) {
        LOG.info("addContactToResume()...");
        handleResultSet(rs, "contact_value",
                value -> {
                    resume.addContact(ContactType.valueOf(rs.getString("contact_type")), value);
                    LOG.info("addContactToResume(): Finish! - [%s]".formatted(value));
                });
    }

    public final void addSectionToResume(ResultSet rs, Resume resume) {
        LOG.info("addSectionToResume()...");
        handleResultSet(rs, "section_value",
                value -> {
                    SectionType sectionType = SectionType.valueOf(rs.getString("section_type").trim());
                    switch (sectionType) {
                        case OBJECTIVE, PERSONAL -> resume.addSection(sectionType, new TextSection(value));
                        case ACHIEVEMENT, QUALIFICATION ->
                                resume.addSection(sectionType, new ListSection(value.split("\n")));
                    }
                    LOG.info("addSectionToResume(): Finish! [%s]: [%s]".formatted(sectionType, value));
                });
    }

    public final void handleQueryResultRows(ResultSet rs, Map<String,
            Resume> resumeMap, ConsumerWithSqlException<Resume> executor) {
        try {
            while (rs.next()) {
                Resume resume = resumeMap.get(rs.getString("resume_uuid"));
                if (resume != null) {
                    executor.execute(resume);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Occurred SqlException", e);
            throw new StorageException(e);
        }
    }

    private void handlePreparedStatement(PreparedStatement ps, String data,
                                         RunnableWithSqlException executor) {
        try {
            executor.execute();
            LOG.info("all addBatch() in loop for(): Finish!");
            ps.executeBatch();
            LOG.info("executeBatch(): Finish!");
        } catch (SQLException e) {
            LOG.log(Level.WARNING, ("SQLException occurred while trying to " +
                    "add [%s] from Resume into table".formatted(data)), e);
            throw new StorageException(e);
        }
    }

    private void handleResultSet(ResultSet rs, String columnValue,
                                 ConsumerWithSqlException<String> executor) {
        try {
            String value = rs.getString(columnValue);
            if (value != null) {
                executor.execute(value);
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQLException occurred while trying to get " +
                    "columns 'type' and 'value' from the ResulSet", e);
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