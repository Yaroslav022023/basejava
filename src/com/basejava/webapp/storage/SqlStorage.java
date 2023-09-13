package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.ConnectionFactory;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
        return sqlHelper.executePreparedStatement(
                "SELECT * " +
                        "  FROM resume " +
                        " ORDER BY full_name, uuid",
                ps -> {
                    LOG.info("getAllSorted: Handling request...");
                    final ResultSet rs = ps.executeQuery();
                    final List<Resume> resumeList = new ArrayList<>();
                    while (rs.next()) {
                        Resume resume = new Resume(rs.getString("uuid"),
                                rs.getString("full_name"));
                        sqlHelper.executePreparedStatement(
                                "SELECT type, value " +
                                        "  FROM contact " +
                                        " WHERE resume_uuid = ?",
                                ps1 -> {
                                    LOG.info("get contacts: Handling request...");
                                    ps1.setString(1, rs.getString("uuid"));
                                    ResultSet resultSet = ps1.executeQuery();
                                    while (resultSet.next()) {
                                        resume.addContact(ContactType.valueOf(resultSet.getString("type")),
                                                resultSet.getString("value"));
                                    }
                                    LOG.info("get contacts: Finish!!!");
                                    return null;
                                });
                        resumeList.add(resume);
                    }
                    LOG.info("getAllSorted: Finish!!!");
                    return resumeList;
                });
    }

    @Override
    public int getSize() {
        return sqlHelper.executePreparedStatement(
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
        sqlHelper.transactionalExecute(
                "INSERT INTO resume (uuid, full_name) " +
                        "VALUES (?,?)",
                ps -> {
                    LOG.info("save: Handling request...");
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, resume.getFullName());
                    ps.execute();

                    sqlHelper.executePreparedStatement(
                            "INSERT INTO contact (resume_uuid, type, value) " +
                                    "VALUES (?,?,?)",
                            ps1 -> {
                                LOG.info("save contacts: Handling request...");
                                sqlHelper.iterateAndBatchExecute(resume.getAllContacts(), ps1,
                                        entry -> {
                                            ps1.setString(1, resume.getUuid());
                                            ps1.setString(2, entry.getKey().name());
                                            ps1.setString(3, entry.getValue());
                                        });
                                LOG.info("Finish save contacts [%s]".formatted(resume.getAllContacts()));
                                return null;
                            });
                    LOG.info("save: Finish save resume [%s]".formatted(resume.toString()));
                    return null;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executePreparedStatement(
                "DELETE FROM resume r " +
                        " WHERE r.uuid = ?",
                ps -> {
                    LOG.info("delete: Handling request...");
                    ps.setString(1, uuid);
                    if (ps.executeUpdate() == 0) {
                        LOG.log(Level.WARNING, "Resume '%s' not exist".formatted(uuid));
                        throw new NotExistStorageException(uuid);
                    }
                    LOG.info("delete: Finish delete resume [%s]".formatted(uuid));
                    return null;
                });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executePreparedStatement(
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
                        String value = rs.getString("value");
                        ContactType type = ContactType.valueOf(rs.getString("type"));
                        resume.addContact(type, value);
                    } while (rs.next());

                    LOG.info("got resume: " + uuid);
                    return resume;
                });
    }

    @Override
    public void clear() {
        sqlHelper.executePreparedStatement("DELETE FROM resume", PreparedStatement::execute);
        LOG.info("cleared");
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(
                "UPDATE resume r " +
                        "   SET full_name = ? " +
                        " WHERE r.uuid = ?",
                ps -> {
                    LOG.info("save: Handling request...");
                    ps.setString(1, resume.getFullName());
                    ps.setString(2, resume.getUuid());
                    if (ps.executeUpdate() == 0) {
                        LOG.log(Level.WARNING, "Resume '" + resume.getUuid() + "' not exist");
                        throw new NotExistStorageException(resume.getUuid());
                    }

                    sqlHelper.executePreparedStatement(
                            "UPDATE contact " +
                                    "   SET value = ?" +
                                    " WHERE resume_uuid = ? " +
                                    "   AND type = ?",
                            ps1 -> {
                                LOG.info("save contacts: Handling request...");
                                sqlHelper.iterateAndBatchExecute(resume.getAllContacts(), ps1,
                                        entry -> {
                                            ps1.setString(1, entry.getValue());
                                            ps1.setString(2, resume.getUuid());
                                            ps1.setString(3, entry.getKey().name());
                                        });
                                LOG.info("Finish update contacts [%s]".formatted(resume.getAllContacts()));
                                return null;
                            });
                    return null;
                });
        LOG.info("updated: " + resume);
    }
}