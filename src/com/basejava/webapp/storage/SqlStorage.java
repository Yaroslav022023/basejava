package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.ConnectionFactory;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(SqlStorage.class.getName());
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        ConnectionFactory connectionFactory = () ->
                DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public List<Resume> getAllSorted() {
        final Map<String, Resume> resumeMap = new LinkedHashMap<>();

        LOG.info("getAllSorted()...");
        sqlHelper.provideConnection(
                "SELECT * " +
                        "  FROM resume r " +
                        " ORDER BY r.full_name, r.uuid",
                ps -> {
                    LOG.info("SELECT table 'resume': Handling request...");
                    final ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String uuid = rs.getString("uuid");
                        resumeMap.put(uuid, new Resume(uuid, rs.getString("full_name")));
                    }
                    return null;
                });

        sqlHelper.provideConnection(
                "SELECT " +
                        "     c.resume_uuid, " +
                        "     c.type AS contact_type, " +
                        "     c.value AS contact_value " +
                        "  FROM contact c ",
                ps -> {
                    LOG.info("SELECT table 'contact': Handling request...");
                    final ResultSet rs = ps.executeQuery();
                    sqlHelper.handleQueryResultRows(rs, resumeMap,
                            resume -> sqlHelper.addContactToResume(rs, resume));
                    LOG.info("SELECT table 'contact': Finish!");
                    return null;
                });

        sqlHelper.provideConnection(
                "SELECT " +
                        "     s.resume_uuid, " +
                        "     s.type AS section_type, " +
                        "     s.value AS section_value " +
                        "  FROM section s ",
                ps -> {
                    LOG.info("SELECT table 'section': Handling request...");
                    final ResultSet rs = ps.executeQuery();
                    sqlHelper.handleQueryResultRows(rs, resumeMap,
                            resume -> sqlHelper.addSectionToResume(rs, resume));
                    LOG.info("SELECT table 'section': Finish!");
                    return null;
                });
        LOG.info("getAllSorted(): Finish!!!");
        return new ArrayList<>(resumeMap.values());
    }

    @Override
    public int getSize() {
        return sqlHelper.provideConnection(
                "SELECT COUNT(*) " +
                        "  FROM resume",
                ps -> {
                    LOG.info("getSize: Handling request...");
                    final ResultSet rs = ps.executeQuery();
                    final int size = rs.next() ? rs.getInt(1) : 0;
                    LOG.info("getSize: Finish!!! [size = %s]".formatted(size));
                    return size;
                });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            sqlHelper.executePreparedStatement(conn,
                    "INSERT INTO resume (uuid, full_name) " +
                            "VALUES (?,?)",
                    ps -> {
                        LOG.info("save: Handling request...");
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, resume.getFullName());
                        ps.execute();
                        LOG.info("save: saved uuid, full_name.");
                        return null;
                    });

            sqlHelper.executePreparedStatement(conn,
                    "INSERT INTO contact (resume_uuid, type, value) " +
                            "VALUES (?,?,?)",
                    ps -> {
                        LOG.info("save contacts: Handling request...");
                        sqlHelper.iterateContactsAndBatchExecute(resume, ps);
                        LOG.info("save: saved contacts for resume: [%s]".formatted(resume.getContacts()));
                        return null;
                    });

            sqlHelper.executePreparedStatement(conn,
                    "INSERT INTO section (type, value, resume_uuid) " +
                            " VALUES (?,?,?)",
                    ps -> {
                        LOG.info("save sections: Handling request...");
                        sqlHelper.iterateSectionsAndBatchExecute(resume, ps);
                        LOG.info("save: saved sections for resume: [%s]".formatted(resume.getSections()));
                        return null;
                    });
            LOG.info("save: Finish! Saved resume [%s]".formatted(resume.toString()));
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.transactionalExecute(conn -> sqlHelper.executePreparedStatement(conn,
                "DELETE FROM resume r " +
                        " WHERE r.uuid = ?",
                ps -> {
                    LOG.info("delete: Handling request...");
                    ps.setString(1, uuid);
                    if (ps.executeUpdate() == 0) {
                        LOG.log(Level.WARNING, "Resume '%s' not exist".formatted(uuid));
                        throw new NotExistStorageException(uuid);
                    }
                    LOG.info("delete: Finish! Deleted resume [%s]".formatted(uuid));
                    return null;
                }));
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.provideConnection(
                "SELECT " +
                        "     r.*, " +
                        "     c.type AS contact_type, " +
                        "     c.value AS contact_value, " +
                        "     s.type AS section_type, " +
                        "     s.value AS section_value " +
                        "  FROM resume r " +
                        "  LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                        "  LEFT JOIN section s ON r.uuid = s.resume_uuid " +
                        " WHERE r.uuid = ?",
                ps -> {
                    LOG.info("get: Handling request...");
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        LOG.log(Level.WARNING, "Resume '" + uuid + "' not exist");
                        throw new NotExistStorageException(uuid);
                    }
                    final Resume resume = new Resume(uuid, rs.getString("full_name"));
                    do {
                        sqlHelper.addContactToResume(rs, resume);
                        sqlHelper.addSectionToResume(rs, resume);
                    } while (rs.next());
                    LOG.info("get: Finish! Got resume: " + uuid);
                    return resume;
                });
    }

    @Override
    public void clear() {
        LOG.info("clear: Handling request...");
        sqlHelper.transactionalExecute(conn -> sqlHelper.executePreparedStatement(conn,
                "DELETE FROM resume", PreparedStatement::execute));
        LOG.info("clear: Finish! Cleared.");
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(conn ->
                sqlHelper.executePreparedStatement(conn,
                        "UPDATE resume r " +
                                "   SET full_name = ? " +
                                " WHERE r.uuid = ?",
                        ps -> {
                            LOG.info("update: Handling request...");
                            ps.setString(1, resume.getFullName());
                            ps.setString(2, resume.getUuid());
                            if (ps.executeUpdate() == 0) {
                                LOG.log(Level.WARNING, "Resume '" +
                                        resume.getUuid() + "' not exist");
                                throw new NotExistStorageException(resume.getUuid());
                            }

                            sqlHelper.executePreparedStatement(conn,
                                    "DELETE FROM contact " +
                                            " WHERE resume_uuid = ?",
                                    ps1 -> {
                                        LOG.info("deleting contacts: Handling request...");
                                        ps1.setString(1, resume.getUuid());
                                        ps1.executeUpdate();
                                        LOG.info(("deleting contacts: Finish! Deleted contacts" +
                                                " for resume_uuid [%s]").formatted(resume.getUuid()));
                                        return null;
                                    });

                            sqlHelper.executePreparedStatement(conn,
                                    "INSERT INTO contact (resume_uuid, type, value) " +
                                            "VALUES (?, ?, ?)",
                                    ps1 -> {
                                        LOG.info("inserting contacts: Handling request...");
                                        sqlHelper.iterateContactsAndBatchExecute(resume, ps1);
                                        LOG.info("inserting contacts: Finish! Inserted contacts [%s]".
                                                formatted(resume.getContacts()));
                                        return null;
                                    });

                            sqlHelper.executePreparedStatement(conn,
                                    "DELETE FROM section " +
                                            " WHERE resume_uuid = ?",
                                    ps1 -> {
                                        LOG.info("deleting sections: Handling request...");
                                        ps1.setString(1, resume.getUuid());
                                        ps1.executeUpdate();
                                        LOG.info(("deleting sections: Finish! Deleted sections" +
                                                " for resume_uuid [%s]").formatted(resume.getUuid()));
                                        return null;
                                    });

                            sqlHelper.executePreparedStatement(conn,
                                    "INSERT INTO section (type, value, resume_uuid)" +
                                            "VALUES (?, ?, ?)",
                                    ps1 -> {
                                        LOG.info("inserting sections: Handling request...");
                                        sqlHelper.iterateSectionsAndBatchExecute(resume, ps1);
                                        LOG.info("inserting sections: Finish!");
                                        return null;
                                    });
                            LOG.info("update: Finish! Updated: " + resume);
                            return null;
                        }));
    }
}