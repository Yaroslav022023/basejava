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

    public <T> T provideConnection(String sqlQuery, PreparedStatementExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error during provideConnection()", e);
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

    public <T> T executePreparedStatement(Connection conn, String sqlQuery, PreparedStatementExecutor<T> executor) {
        try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            LOG.info("Got a connection to the database.");
            return executor.execute(ps);
        } catch (SQLException e) {
            handleSQLException(e);
            throw new StorageException(e);
        }
    }

    public void iterateContactsAndBatchExecute(Resume resume, PreparedStatement ps) {
        try {
            LOG.info("iterateContactsAndBatchExecute()...");
            for (Map.Entry<ContactType, String> entry : resume.getAllContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, entry.getValue());
                ps.addBatch();
            }
            LOG.info("all addBatch() in loop for(): Finish!");
            ps.executeBatch();
            LOG.info("executeBatch(): Finish!");
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Occurred SqlException", e);
            throw new StorageException(e);
        }
    }

    public void addContact(ResultSet rs, Resume resume) {
        try {
            LOG.info("addContact()...");
            String value = rs.getString("contact_value");
            if (value != null) {
                resume.addContact(ContactType.valueOf(rs.getString("contact_type")), value);
                LOG.info("addContact(): Finish! - [%s]".formatted(value));
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQLException occurred while trying to get " +
                    "column 'contact_value' from the database", e);
            throw new StorageException(e);
        }
    }

    public void saveSection(Resume resume, PreparedStatement ps) {
        SectionType objective = SectionType.OBJECTIVE;
        SectionType personal = SectionType.PERSONAL;
        try {
            ps.setString(1, resume.getUuid());
            LOG.info("saveSection(): OBJECTIVE...");
            ps.setString(2, resume.getSection(objective) != null ?
                    resume.getSection(objective).toString() : null);
            LOG.info("saveSection(): PERSONAL...");
            ps.setString(3, resume.getSection(personal) != null ?
                    resume.getSection(personal).toString() : null);
            ps.executeUpdate();
            LOG.info("saveSection(): Finish! For resume_uuid: [%s]".formatted(resume.getUuid()));
        } catch (SQLException e) {
            LOG.log(Level.WARNING, ("SQLException occurred while trying to save " +
                    "sections in the table 'section'"), e);
            throw new StorageException(e);
        }
    }

    public void addSectionToResume(ResultSet rs, Resume resume) {
        SectionType[] sectionTypes = new SectionType[]{SectionType.OBJECTIVE, SectionType.PERSONAL};
        for (SectionType sectionType : sectionTypes) {
            if (resume.getSection(sectionType) == null) {
                try {
                    LOG.info("addSectionToResume(): [%s]...".formatted(sectionType));
                    String text = rs.getString(sectionType.name().toLowerCase());
                    if (text != null) {
                        resume.addSection(sectionType, new TextSection(text));
                        LOG.info("addSectionToResume() [%s]: Finish!".formatted(sectionType));
                    }
                } catch (SQLException e) {
                    LOG.log(Level.WARNING, ("SQLException occurred while trying to get " +
                            "sections from the table 'section'"), e);
                    throw new StorageException(e);
                }
            }
        }
    }

    public void updateSection(PreparedStatement ps, Resume resume) {
        SectionType objective = SectionType.OBJECTIVE;
        SectionType personal = SectionType.PERSONAL;
        try {
            LOG.info("updating [%s]...".formatted(objective));
            ps.setString(1, resume.getSection(objective) != null ?
                    resume.getSection(objective).toString() : null);
            LOG.info("updating [%s]...".formatted(personal));
            ps.setString(2, resume.getSection(personal) != null ?
                    resume.getSection(personal).toString() : null);
            ps.setString(3, resume.getUuid());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.WARNING, ("SQLException occurred while trying to update " +
                    "sections in the table 'section'"), e);
            throw new StorageException(e);
        }
        LOG.info(("updateSection(): Finish! For resume_uuid: [%s]").formatted(resume.getUuid()));
    }

    public void handleQueryResultRows(ResultSet rs, Map<String, Resume> resumeMap, RunnableWithSqlException executor) {
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

    private void handleSQLException(SQLException e) {
        if ("23505".equals(e.getSQLState())) {
            LOG.log(Level.WARNING, "This resume already exists !!!", e);
            throw new ExistStorageException(null);
        }
        LOG.log(Level.WARNING, "Occurred SqlException", e);
    }
}