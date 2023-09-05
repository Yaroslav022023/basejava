package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.ConnectionFactory;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                "SELECT * FROM resume ORDER BY full_name, uuid", ps -> {
                    List<Resume> resumes = new ArrayList<>();
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        resumes.add(new Resume(rs.getString("uuid"),
                                rs.getString("full_name")));
                    }
                    LOG.info("getAllSorted - done");
                    return resumes;
                });
    }

    @Override
    public int getSize() {
        return sqlHelper.executePreparedStatement(
                "SELECT COUNT(*) FROM resume", ps -> {
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        LOG.info("returned size resume database");
                        return rs.getInt(1);
                    }
                    LOG.info("returned size '0' resume database");
                    return 0;
                });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.executePreparedStatement(
                "INSERT INTO resume (uuid, full_name) VALUES (?,?)", ps -> {
                    try {
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, resume.getFullName());
                        return ps.execute();
                    } catch (SQLException e) {
                        sqlHelper.checkExistingResume(e, resume.getUuid());
                        return null;
                    }
                });
        LOG.info("saved: " + resume);
    }

    @Override
    public void delete(String uuid) {
        Integer affectedRows = sqlHelper.executePreparedStatement(
                "DELETE FROM resume r WHERE r.uuid = ?", ps -> {
                    ps.setString(1, uuid);
                    return ps.executeUpdate();
                });

        if (affectedRows == 0) {
            LOG.log(Level.WARNING, "Resume '" + uuid + "' not exist");
            throw new NotExistStorageException(uuid);
        }
        LOG.info("deleted: " + uuid);
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executePreparedStatement(
                "SELECT * FROM resume r WHERE r.uuid = ?", ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        LOG.log(Level.WARNING, "Resume '" + uuid + "' not exist");
                        throw new NotExistStorageException(uuid);
                    }
                    LOG.info("got resume: " + uuid);
                    return new Resume(uuid, rs.getString("full_name"));
                });
    }

    @Override
    public void clear() {
        sqlHelper.executePreparedStatement(
                "DELETE FROM resume", PreparedStatement::execute);
        LOG.info("cleared");
    }

    @Override
    public void update(Resume resume) {
        Integer affectedRows = sqlHelper.executePreparedStatement(
                "UPDATE resume r SET full_name = ? WHERE r.uuid = ?", ps -> {
                    ps.setString(1, resume.getFullName());
                    ps.setString(2, resume.getUuid());
                    return ps.executeUpdate();
                });

        if (affectedRows == 0) {
            LOG.log(Level.WARNING, "Resume '" + resume.getUuid() + "' not exist");
            throw new NotExistStorageException(resume.getUuid());
        }
        LOG.info("updated: " + resume);
    }
}