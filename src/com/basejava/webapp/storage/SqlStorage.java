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
        return sqlHelper.provideConnection(conn -> sqlHelper.executePreparedStatement(conn,
                "SELECT * " +
                        "  FROM resume r " +
                        "  LEFT JOIN contact c " +
                        "    ON r.uuid = c.resume_uuid " +
                        " ORDER BY r.full_name, r.uuid",
                ps -> {
                    LOG.info("getAllSorted: Handling request...");
                    final ResultSet rs = ps.executeQuery();
                    final Map<String, Resume> resumeMap = new LinkedHashMap<>();
                    while (rs.next()) {
                        String uuid = rs.getString("uuid");
                        boolean existResume = resumeMap.containsKey(uuid);
                        final Resume resume = existResume ?
                                resumeMap.get(uuid) :
                                new Resume(uuid, rs.getString("full_name"));

                        LOG.info("get contact: Handling request...");
                        sqlHelper.addContact(rs, resume);
                        LOG.info("get contact: Finish for resume: %s".formatted(uuid));
                        if (!existResume) {
                            resumeMap.put(uuid, resume);
                        }
                    }
                    LOG.info("getAllSorted: Finish!!!");
                    return new ArrayList<>(resumeMap.values());
                }));

    }

    @Override
    public int getSize() {
        return sqlHelper.provideConnection(conn -> sqlHelper.executePreparedStatement(conn,
                "SELECT COUNT(*) " +
                        "  FROM resume",
                ps -> {
                    LOG.info("getSize: Handling request...");
                    try {
                        final ResultSet rs = ps.executeQuery();
                        final int size = rs.next() ? rs.getInt(1) : 0;
                        LOG.info("getSize: Finish!!! [size = %s]".formatted(size));
                        return size;
                    } finally {
                        conn.close();
                    }
                }));
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
                        LOG.info("save: saved contacts [%s]".formatted(resume.getAllContacts()));
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
        return sqlHelper.provideConnection(conn -> sqlHelper.executePreparedStatement(conn,
                "SELECT * " +
                        "  FROM resume r " +
                        "  LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
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
                        sqlHelper.addContact(rs, resume);
                    } while (rs.next());
                    LOG.info("get: Finish! Got resume: " + uuid);
                    return resume;
                }));
    }

    @Override
    public void clear() {
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
                                                formatted(resume.getAllContacts()));
                                        return null;
                                    });
                            LOG.info("update: Finish! Updated: " + resume);
                            return null;
                        }));
    }
}